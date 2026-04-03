package com.rdapps.aboutme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

fun String.toAnnotated() = AnnotatedString.fromHtml(this)

/**
 * Converts a string with HTML tags into [AnnotatedString].
 *
 * Supported HTML tags:
 * - `<sb>`                                  – semibold
 * - `<b>`, `<strong>`                       – bold
 * - `<i>`, `<em>`, `<cite>`, `<dfn>`        – italic
 * - `<u>`                                   – underline
 * - `<s>`, `<strike>`, `<del>`              – strikethrough
 * - `<sup>`                                 – superscript
 * - `<sub>`                                 – subscript
 * - `<tt>`, `<code>`                        – monospace
 * - `<big>`                                 – larger text (1.25em)
 * - `<small>`                               – smaller text (0.8em)
 * - `<h1>`–`<h6>`                           – headings (bold + sized)
 * - `<font color="…" face="…" size="1–7">` – font colour / typeface / relative size
 * - `<span style="…">`                      – inline CSS (color, background-color,
 *                                              font-weight, font-style, text-decoration,
 *                                              font-size, font-family)
 * - `<a href="…">`                          – hyperlinks
 * - `<p align="…">`, `<div>`               – block paragraphs / alignment
 * - `<br>`                                  – line break
 * - `<ul>`, `<li>`                          – unordered bullet lists (nested)
 * - `<annotation key="value" …>`            – custom [AnnotatedString] string annotations
 *
 * HTML character entities (`&amp;` `&lt;` `&gt;` `&quot;` `&apos;` `&nbsp;`
 * `&#N;` `&#xN;`) are decoded automatically.
 *
 * @param htmlString              The HTML-tagged string to parse.
 * @param linkStyles              Optional style configuration applied to `<a>` links.
 * @param linkInteractionListener Optional click listener for `<a>` links. When `null`
 *                                the platform's default URI handler is used.
 */
fun AnnotatedString.Companion.fromHtml(
    htmlString: String,
    linkStyles: TextLinkStyles? = null,
    linkInteractionListener: LinkInteractionListener? = null,
): AnnotatedString =
    HtmlToAnnotatedStringParser(htmlString, linkStyles, linkInteractionListener).parse()

// ─────────────────────────────────────────────────────────────────────────────
// Internal implementation
// ─────────────────────────────────────────────────────────────────────────────

private class HtmlToAnnotatedStringParser(
    private val html: String,
    private val linkStyles: TextLinkStyles?,
    private val linkInteractionListener: LinkInteractionListener?,
) {
    private val builder = AnnotatedString.Builder()

    // Each entry: (tagName, startIndexInBuilder, attributes)
    private val tagStack = ArrayDeque<TagEntry>()
    private var bulletDepth = 0

    private data class TagEntry(
        val name: String,
        val start: Int,
        val attrs: Map<String, String>,
    )

    // ── Public entry point ───────────────────────────────────────────────────

    fun parse(): AnnotatedString {
        tokenize(html).forEach { token ->
            when (token) {
                is HtmlToken.Text -> builder.append(decodeEntities(token.content))
                is HtmlToken.Open -> handleOpen(token.name, token.attrs)
                is HtmlToken.Close -> handleClose(token.name)
                is HtmlToken.SelfClose -> handleSelfClose(token.name, token.attrs)
            }
        }
        return builder.toAnnotatedString()
    }

    // ── Tag handlers ─────────────────────────────────────────────────────────

    private fun handleOpen(rawName: String, attrs: Map<String, String>) {
        val name = rawName.lowercase()
        val start = builder.length
        when (name) {
            "br" -> builder.append('\n')
            "p", "div" -> {
                ensureNewline()
                tagStack.addLast(TagEntry(name, start, attrs))
            }

            "ul" -> {
                bulletDepth++
                tagStack.addLast(TagEntry(name, start, attrs))
            }

            "li" -> {
                ensureNewline()
                val bullet = when (bulletDepth) {
                    1 -> "• "
                    2 -> "◦ "
                    else -> "▪ "
                }
                val indent = "  ".repeat((bulletDepth - 1).coerceAtLeast(0))
                builder.append(indent + bullet)
                tagStack.addLast(TagEntry(name, builder.length, attrs))
            }

            else -> tagStack.addLast(TagEntry(name, start, attrs))
        }
    }

    private fun handleClose(rawName: String) {
        val name = rawName.lowercase()
        // Find the most recent matching tag (handles mild mis-nesting gracefully)
        val index = tagStack.indexOfLast { it.name == name }
        if (index < 0) return
        val entry = tagStack.removeAt(index)
        val start = entry.start
        val end = builder.length

        when (name) {
            "sb" -> builder.addStyle(SpanStyle(fontWeight = FontWeight.SemiBold), start, end)
            "b", "strong" ->
                builder.addStyle(SpanStyle(fontWeight = FontWeight.SemiBold), start, end)

            "i", "em", "cite", "dfn" ->
                builder.addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)

            "u" ->
                builder.addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)

            "s", "strike", "del" ->
                builder.addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)

            "sup" ->
                builder.addStyle(SpanStyle(baselineShift = BaselineShift.Superscript), start, end)

            "sub" ->
                builder.addStyle(SpanStyle(baselineShift = BaselineShift.Subscript), start, end)

            "tt", "code" ->
                builder.addStyle(SpanStyle(fontFamily = FontFamily.Monospace), start, end)

            "big" ->
                builder.addStyle(SpanStyle(fontSize = 1.25f.em), start, end)

            "small" ->
                builder.addStyle(SpanStyle(fontSize = 0.8f.em), start, end)

            "h1" -> applyHeading(start, end, fontSize = 2.0f, textAlign = TextAlign.Center)
            "h2" -> applyHeading(start, end, fontSize = 1.5f, textAlign = TextAlign.Center)
            "h3" -> applyHeading(start, end, fontSize = 1.17f)
            "h4" -> applyHeading(start, end, fontSize = 1.0f)
            "h5" -> applyHeading(start, end, fontSize = 0.83f)
            "h6" -> applyHeading(start, end, fontSize = 0.67f)

            "font" -> applyFontTag(entry.attrs, start, end)

            "span" -> applySpanTag(entry.attrs, start, end)

            "a" -> applyAnchorTag(entry.attrs, start, end)

            "p", "div" -> {
                applyAlignment(entry.attrs["align"], start, end)
                ensureNewline()
            }

            "li" -> ensureNewline()

            "ul" -> {
                bulletDepth = (bulletDepth - 1).coerceAtLeast(0)
            }

            "annotation" -> applyAnnotations(entry.attrs, start, end)
        }
    }

    private fun handleSelfClose(rawName: String, attrs: Map<String, String>) {
        when (rawName.lowercase()) {
            "br" -> builder.append('\n')
            else -> handleOpen(rawName, attrs)
        }
    }

    // ── Style application helpers ────────────────────────────────────────────

    private fun applyHeading(
        start: Int,
        end: Int,
        fontSize: Float,
        textAlign: TextAlign = TextAlign.Unspecified,
    ) {
        builder.addStyle(
            SpanStyle(fontWeight = FontWeight.Bold, fontSize = fontSize.em),
            start, end,
        )
        if (textAlign != TextAlign.Unspecified) {
            builder.addStyle(ParagraphStyle(textAlign = textAlign), start, end)
        }
        ensureNewline()
    }

    private fun applyFontTag(attrs: Map<String, String>, start: Int, end: Int) {
        attrs["color"]?.let { colorStr ->
            parseColor(colorStr)?.let { color ->
                builder.addStyle(SpanStyle(color = color), start, end)
            }
        }
        attrs["face"]?.let { face ->
            val fontFamily = namedFontFamily(face) ?: FontFamily.Default
            builder.addStyle(SpanStyle(fontFamily = fontFamily), start, end)
        }
        attrs["size"]?.trim()?.toIntOrNull()?.let { size ->
            // HTML font size 1–7 mapped to relative em sizes
            val em = htmlFontSizeToEm(size)
            builder.addStyle(SpanStyle(fontSize = em.em), start, end)
        }
    }

    private fun applySpanTag(attrs: Map<String, String>, start: Int, end: Int) {
        val styleAttr = attrs["style"] ?: return
        val css = parseCssStyles(styleAttr)
        var spanStyle = SpanStyle()

        css["color"]?.let { parseColor(it) }
            ?.let { spanStyle = spanStyle.merge(SpanStyle(color = it)) }
        css["background-color"]?.let { parseColor(it) }
            ?.let { spanStyle = spanStyle.merge(SpanStyle(background = it)) }
        css["font-weight"]?.let {
            when (it.lowercase()) {
                "bold", "700", "800", "900" ->
                    spanStyle = spanStyle.merge(SpanStyle(fontWeight = FontWeight.Bold))

                "normal", "400" ->
                    spanStyle = spanStyle.merge(SpanStyle(fontWeight = FontWeight.Normal))

                else -> it.toIntOrNull()?.let { w ->
                    spanStyle = spanStyle.merge(SpanStyle(fontWeight = FontWeight(w)))
                }
            }
        }
        css["font-style"]?.let {
            when (it.lowercase()) {
                "italic" -> spanStyle = spanStyle.merge(SpanStyle(fontStyle = FontStyle.Italic))
                "normal" -> spanStyle = spanStyle.merge(SpanStyle(fontStyle = FontStyle.Normal))
            }
        }
        css["text-decoration"]?.let {
            when {
                "underline" in it -> spanStyle =
                    spanStyle.merge(SpanStyle(textDecoration = TextDecoration.Underline))

                "line-through" in it -> spanStyle =
                    spanStyle.merge(SpanStyle(textDecoration = TextDecoration.LineThrough))
            }
        }
        css["font-size"]?.let {
            parseCssFontSize(it)?.let { size ->
                spanStyle = spanStyle.merge(SpanStyle(fontSize = size))
            }
        }
        css["font-family"]?.let {
            val ff = namedFontFamily(it.trim().removeSurrounding("\"").removeSurrounding("'"))
            if (ff != null) spanStyle = spanStyle.merge(SpanStyle(fontFamily = ff))
        }
        if (spanStyle != SpanStyle()) {
            builder.addStyle(spanStyle, start, end)
        }
    }

    private fun applyAnchorTag(attrs: Map<String, String>, start: Int, end: Int) {
        val url = attrs["href"] ?: return
        val link = LinkAnnotation.Url(url, linkStyles, linkInteractionListener)
        builder.addLink(link, start, end)
    }

    private fun applyAnnotations(attrs: Map<String, String>, start: Int, end: Int) {
        attrs.forEach { (key, value) ->
            builder.addStringAnnotation(key, value, start, end)
        }
    }

    private fun applyAlignment(align: String?, start: Int, end: Int) {
        val textAlign = when (align?.lowercase()) {
            "center" -> TextAlign.Center
            "right" -> TextAlign.End
            "left" -> TextAlign.Start
            "justify" -> TextAlign.Justify
            else -> return
        }
        builder.addStyle(ParagraphStyle(textAlign = textAlign), start, end)
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /** Appends '\n' only if the builder doesn't already end with one. */
    private fun ensureNewline() {
        if (builder.length > 0 && builder.toString().last() != '\n') {
            builder.append('\n')
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tokenizer
    // ─────────────────────────────────────────────────────────────────────────

    private fun tokenize(input: String): List<HtmlToken> {
        val tokens = mutableListOf<HtmlToken>()
        var i = 0
        val len = input.length

        while (i < len) {
            if (input[i] == '<') {
                // Possibly a comment or tag
                if (input.startsWith("<!--", i)) {
                    // Skip HTML comment
                    val end = input.indexOf("-->", i + 4)
                    i = if (end < 0) len else end + 3
                    continue
                }
                val tagEnd = input.indexOf('>', i)
                if (tagEnd < 0) {
                    // Malformed – treat rest as text
                    tokens.add(HtmlToken.Text(input.substring(i)))
                    break
                }
                val tagContent = input.substring(i + 1, tagEnd).trim()
                i = tagEnd + 1

                when {
                    tagContent.startsWith('/') -> {
                        val name = tagContent.substring(1).trim()
                        tokens.add(HtmlToken.Close(name))
                    }

                    tagContent.endsWith('/') -> {
                        val body = tagContent.dropLast(1).trim()
                        val (name, attrs) = parseTag(body)
                        tokens.add(HtmlToken.SelfClose(name, attrs))
                    }

                    else -> {
                        val (name, attrs) = parseTag(tagContent)
                        tokens.add(HtmlToken.Open(name, attrs))
                    }
                }
            } else {
                val tagStart = input.indexOf('<', i)
                val textEnd = if (tagStart < 0) len else tagStart
                tokens.add(HtmlToken.Text(input.substring(i, textEnd)))
                i = textEnd
            }
        }
        return tokens
    }

    /** Splits a raw tag body (e.g. `a href="url"`) into name + attribute map. */
    private fun parseTag(body: String): Pair<String, Map<String, String>> {
        val spaceIdx = body.indexOfFirst { it.isWhitespace() }
        val name = if (spaceIdx < 0) body else body.substring(0, spaceIdx)
        val attrStr = if (spaceIdx < 0) "" else body.substring(spaceIdx + 1)
        return name.lowercase() to parseAttributes(attrStr)
    }

    /** Parses an HTML attribute string into a map of lowercased-key → value pairs. */
    private fun parseAttributes(attrStr: String): Map<String, String> {
        val attrs = mutableMapOf<String, String>()
        var i = 0
        val len = attrStr.length

        while (i < len) {
            // Skip whitespace
            while (i < len && attrStr[i].isWhitespace()) i++
            if (i >= len) break

            // Read name
            val nameStart = i
            while (i < len && attrStr[i] != '=' && !attrStr[i].isWhitespace()) i++
            val name = attrStr.substring(nameStart, i).trim().lowercase()
            if (name.isEmpty()) {
                i++; continue
            }

            // Skip whitespace
            while (i < len && attrStr[i].isWhitespace()) i++

            if (i >= len || attrStr[i] != '=') {
                attrs[name] = ""
                continue
            }
            i++ // skip '='
            while (i < len && attrStr[i].isWhitespace()) i++

            val value = when {
                i < len && attrStr[i] == '"' -> {
                    i++
                    val s = i
                    while (i < len && attrStr[i] != '"') i++
                    val v = attrStr.substring(s, i)
                    if (i < len) i++
                    v
                }

                i < len && attrStr[i] == '\'' -> {
                    i++
                    val s = i
                    while (i < len && attrStr[i] != '\'') i++
                    val v = attrStr.substring(s, i)
                    if (i < len) i++
                    v
                }

                else -> {
                    val s = i
                    while (i < len && !attrStr[i].isWhitespace()) i++
                    attrStr.substring(s, i)
                }
            }
            attrs[name] = value
        }
        return attrs
    }

    /** Parses a CSS style attribute value into property → value pairs. */
    private fun parseCssStyles(style: String): Map<String, String> =
        style.split(';')
            .mapNotNull { decl ->
                val colon = decl.indexOf(':')
                if (colon < 0) null
                else decl.substring(0, colon).trim().lowercase() to decl.substring(colon + 1).trim()
            }
            .toMap()
}

// ─────────────────────────────────────────────────────────────────────────────
// Token model
// ─────────────────────────────────────────────────────────────────────────────

private sealed interface HtmlToken {
    data class Text(val content: String) : HtmlToken
    data class Open(val name: String, val attrs: Map<String, String>) : HtmlToken
    data class Close(val name: String) : HtmlToken
    data class SelfClose(val name: String, val attrs: Map<String, String>) : HtmlToken
}

// ─────────────────────────────────────────────────────────────────────────────
// Color helpers
// ─────────────────────────────────────────────────────────────────────────────

private fun parseColor(raw: String): Color? {
    val s = raw.trim().lowercase()
    return when {
        s.startsWith('#') -> parseHexColor(s.drop(1))
        s.startsWith("rgb(") -> parseRgbColor(s)
        else -> HTML_NAMED_COLORS[s]
    }
}

private fun parseHexColor(hex: String): Color? = when (hex.length) {
    3 -> {
        val r = hex[0].digitToIntOrNull(16) ?: return null
        val g = hex[1].digitToIntOrNull(16) ?: return null
        val b = hex[2].digitToIntOrNull(16) ?: return null
        Color(r * 17, g * 17, b * 17)
    }

    6 -> {
        val v = hex.toLongOrNull(16) ?: return null
        Color((0xFF000000L or v).toInt())
    }

    8 -> {
        // AARRGGBB
        val v = hex.toLongOrNull(16) ?: return null
        Color(v.toInt())
    }

    else -> null
}

private fun parseRgbColor(s: String): Color? {
    val inner = s.removePrefix("rgb(").removeSuffix(")").trim()
    val parts = inner.split(',').map { it.trim().toIntOrNull() }
    if (parts.size < 3 || parts.any { it == null }) return null
    return Color(parts[0]!!, parts[1]!!, parts[2]!!)
}

private val HTML_NAMED_COLORS: Map<String, Color> = mapOf(
    "black" to Color(0xFF000000.toInt()),
    "white" to Color(0xFFFFFFFF.toInt()),
    "red" to Color(0xFFFF0000.toInt()),
    "lime" to Color(0xFF00FF00.toInt()),
    "green" to Color(0xFF008000.toInt()),
    "blue" to Color(0xFF0000FF.toInt()),
    "yellow" to Color(0xFFFFFF00.toInt()),
    "cyan" to Color(0xFF00FFFF.toInt()),
    "aqua" to Color(0xFF00FFFF.toInt()),
    "magenta" to Color(0xFFFF00FF.toInt()),
    "fuchsia" to Color(0xFFFF00FF.toInt()),
    "silver" to Color(0xFFC0C0C0.toInt()),
    "gray" to Color(0xFF808080.toInt()),
    "grey" to Color(0xFF808080.toInt()),
    "maroon" to Color(0xFF800000.toInt()),
    "olive" to Color(0xFF808000.toInt()),
    "navy" to Color(0xFF000080.toInt()),
    "teal" to Color(0xFF008080.toInt()),
    "orange" to Color(0xFFFFA500.toInt()),
    "purple" to Color(0xFF800080.toInt()),
    "brown" to Color(0xFFA52A2A.toInt()),
    "pink" to Color(0xFFFFC0CB.toInt()),
    "gold" to Color(0xFFFFD700.toInt()),
    "coral" to Color(0xFFFF7F50.toInt()),
    "salmon" to Color(0xFFFA8072.toInt()),
    "violet" to Color(0xFFEE82EE.toInt()),
    "indigo" to Color(0xFF4B0082.toInt()),
    "turquoise" to Color(0xFF40E0D0.toInt()),
    "skyblue" to Color(0xFF87CEEB.toInt()),
    "darkblue" to Color(0xFF00008B.toInt()),
    "darkgreen" to Color(0xFF006400.toInt()),
    "darkred" to Color(0xFF8B0000.toInt()),
    "darkgray" to Color(0xFFA9A9A9.toInt()),
    "darkgrey" to Color(0xFFA9A9A9.toInt()),
    "lightgray" to Color(0xFFD3D3D3.toInt()),
    "lightgrey" to Color(0xFFD3D3D3.toInt()),
    "transparent" to Color.Transparent,
)

// ─────────────────────────────────────────────────────────────────────────────
// Font helpers
// ─────────────────────────────────────────────────────────────────────────────

/** Maps a generic font-family name to a [FontFamily], or `null` if unknown. */
private fun namedFontFamily(name: String): FontFamily? = when (name.lowercase().trim()) {
    "monospace", "courier", "courier new" -> FontFamily.Monospace
    "serif", "times", "times new roman", "georgia" -> FontFamily.Serif
    "sans-serif", "sans serif", "helvetica", "arial", "verdana" -> FontFamily.SansSerif
    "cursive", "comic sans ms" -> FontFamily.Cursive
    else -> null
}

/**
 * Maps legacy HTML `<font size="1–7">` to an em multiplier.
 * Based on browser defaults relative to the base size 3.
 */
private fun htmlFontSizeToEm(size: Int): Float = when (size.coerceIn(1, 7)) {
    1 -> 0.63f
    2 -> 0.82f
    3 -> 1.00f
    4 -> 1.13f
    5 -> 1.50f
    6 -> 2.00f
    7 -> 3.00f
    else -> 1.00f
}

/** Parses a CSS font-size value (px, em, rem, %) to a Compose [androidx.compose.ui.unit.TextUnit]. */
private fun parseCssFontSize(value: String): androidx.compose.ui.unit.TextUnit? {
    val v = value.trim().lowercase()
    return when {
        v.endsWith("em") -> v.dropLast(2).toFloatOrNull()?.em
        v.endsWith("rem") -> v.dropLast(3).toFloatOrNull()?.em
        v.endsWith("px") -> v.dropLast(2).toFloatOrNull()?.sp
        v.endsWith("%") -> v.dropLast(1).toFloatOrNull()?.let { (it / 100f).em }
        else -> v.toFloatOrNull()?.sp
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HTML entity decoder
// ─────────────────────────────────────────────────────────────────────────────

private fun decodeEntities(text: String): String {
    if ('&' !in text) return text
    val sb = StringBuilder(text.length)
    var i = 0
    while (i < text.length) {
        if (text[i] != '&') {
            sb.append(text[i++])
            continue
        }
        val semi = text.indexOf(';', i)
        if (semi < 0) {
            sb.append(text[i++]); continue
        }
        val entity = text.substring(i + 1, semi)
        val decoded: String? = when {
            entity.startsWith('#') -> {
                val code = if (entity.startsWith("#x") || entity.startsWith("#X")) {
                    entity.substring(2).toLongOrNull(16)
                } else {
                    entity.substring(1).toLongOrNull()
                }
                code?.toInt()?.toChar()?.toString()
            }

            else -> NAMED_ENTITIES[entity]
        }
        if (decoded != null) {
            sb.append(decoded)
            i = semi + 1
        } else {
            sb.append(text[i++])
        }
    }
    return sb.toString()
}

private val NAMED_ENTITIES: Map<String, String> = mapOf(
    "amp" to "&",
    "lt" to "<",
    "gt" to ">",
    "quot" to "\"",
    "apos" to "'",
    "nbsp" to "\u00A0",
    "copy" to "©",
    "reg" to "®",
    "trade" to "™",
    "mdash" to "—",
    "ndash" to "–",
    "laquo" to "«",
    "raquo" to "»",
    "hellip" to "…",
    "bull" to "•",
    "middot" to "·",
    "euro" to "€",
    "pound" to "£",
    "yen" to "¥",
    "cent" to "¢",
    "deg" to "°",
    "plusmn" to "±",
    "times" to "×",
    "divide" to "÷",
    "frac12" to "½",
    "frac14" to "¼",
    "frac34" to "¾",
    "acute" to "´",
    "grave" to "`",
    "tilde" to "~",
    "cedil" to "¸",
    "uml" to "¨",
    "sup2" to "²",
    "sup3" to "³",
    "sup1" to "¹",
    "alpha" to "α",
    "beta" to "β",
    "gamma" to "γ",
    "delta" to "δ",
    "pi" to "π",
    "sigma" to "σ",
    "omega" to "ω",
)
