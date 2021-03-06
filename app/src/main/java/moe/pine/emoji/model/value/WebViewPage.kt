package moe.pine.emoji.model.value

/**
 * Model for WebViewPage
 * Created by pine on Apr 21, 2017.
 */
enum class WebViewPage(
        val title: String,
        val url: String
) {
    CONTACT("お問い合わせ", "http://app.static.emoji.pine.moe/contact.html"),
    PRIVACY("プライバシーポリシー", "http://app.static.emoji.pine.moe/privacy.html"),
    COPYRIGHT("著作権表記", "http://app.static.emoji.pine.moe/copyright_android.html");

    companion object {
        val urls: List<String> by lazy { WebViewPage.values().map { it.url } }

        fun of(ordinal: Int): WebViewPage? = WebViewPage.values().find { it.ordinal == ordinal }
    }
}