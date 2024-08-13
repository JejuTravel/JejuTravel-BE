from googletrans import Translator
import sys

def translate_text(text, src, dest):
    translator = Translator()
    # None 또는 빈 문자열 체크
    if text is None or text.strip() == "":
        return "未提供翻译文本(No text provided for translation)"
    try:
        translation = translator.translate(text, src=src, dest=dest)
        return translation.text
    except Exception as e:
        print(f"Error occurred during translation: {e}")
        return f"Translation error: {str(e)}"

if __name__ == "__main__":
    if len(sys.argv) > 3:
        text_to_translate = sys.argv[1]
        source_language = sys.argv[2]
        destination_language = sys.argv[3]
        translated_text = translate_text(text_to_translate, source_language, destination_language)
        print(translated_text)
    else:
        print("<text>가 입력되지 않았습니다.")