package com.devixgroup.translatetospanish.model;

public class TranslationUrl {

    private static final String TEXT_TO_TRANSLATE_PLACEHOLDER = "%%_TEXT_TO_TRANSLATE_%%";

    private static final String KEY_PLACEHOLDER = "%%_KEY_%%";

    private static final String TRANSLATION_DIRECTION = "en-es";

    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";

    public static final String REQUEST_URL = BASE_URL +
            "key=" + KEY_PLACEHOLDER +
            "&text=" + TEXT_TO_TRANSLATE_PLACEHOLDER +
            "&lang=" + TRANSLATION_DIRECTION +
            "&format=plain";

    public String mKey;

    public String mTextToTranslate;

    public TranslationUrl(String key, String textToTranslate) {
        this.mKey = key;
        this.mTextToTranslate = textToTranslate;
    }

    public String getRequestUrl() {
        return REQUEST_URL.replace(KEY_PLACEHOLDER, mKey).replace(TEXT_TO_TRANSLATE_PLACEHOLDER, mTextToTranslate);
    }

}
