package main;

public class TypedInputHandler {
    GamePanel gp;
    public String typedText = "";

    public TypedInputHandler(GamePanel gp)
    {
        this.gp = gp;
    }

    public void updateTypedText()
    {
        if (gp.keyH.lastTypedChar != 0) {
            if (gp.keyH.lastTypedChar == '\b' && typedText.length() > 0) {
                typedText = typedText.substring(0, typedText.length() - 1);
            } else if (gp.keyH.lastTypedChar != '\b' && gp.keyH.lastTypedChar >= 32) {
                typedText += gp.keyH.lastTypedChar;
            }
            gp.keyH.lastTypedChar = 0;
        }
    }

    public String getText()
    {
        return typedText;
    }

    public void clearText()
    {
        typedText = "";
    }
}
