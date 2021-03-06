package nposmak.external_api_bot.config;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor

public enum Icons {
    TRAIN(EmojiParser.parseToUnicode(":station:")),
    VAGON(EmojiParser.parseToUnicode(":train:")),
    ARROW(EmojiParser.parseToUnicode(":arrow_right:")),
    X(EmojiParser.parseToUnicode(":x:")),
    HI(EmojiParser.parseToUnicode(":raising_hand:")),
    RED_DOT(EmojiParser.parseToUnicode(":small_orange_diamond:")),
    BLUE_DOT(EmojiParser.parseToUnicode(":small_blue_diamond:"));


    private String iconName;

    @Override
    public String toString() {
        return  iconName ;
    }
}
