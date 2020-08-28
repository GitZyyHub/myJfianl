package com.kakarote.crm9.erp.admin.common;

public enum PinyinEnum{
    /* 汉字每个首字母拼音 第一个和最后一个汉字 */
    A("a","吖","驁"),
    B("b","八","簿"),
    C("c","嚓","错"),
    D("d","咑","鵽"),
    E("e","妸","樲"),
    F("f","发","鰒"),
    G("g","猤","腂"),
    H("h","妎","夻"),
//    I("i","",""),
    J("j","丌","攈"),
    K("k","咔","穒"),
    L("l","垃","鱳"),
    M("m","嘸","旀"),
    N("n","丆","桛"),
    O("o","噢","漚"),
    P("p","妑","曝"),
    Q("q","七","囕"),
    R("r","呥","鶸"),
    S("s","仨","蜶"),
    T("t","他","籜"),
//    U("u","",""),
//    V("v","",""),
    W("w","屲","鶩"),
    X("x","夕","鑂"),
    Y("y","丫","韻"),
    Z("z","帀","咗");

    private String letter;
    private String firstWord;
    private String lastWord;

    public String getLetter() {
        return letter;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public String getLastWord() {
        return lastWord;
    }

    PinyinEnum(String letter, String firstWord, String lastWord) {
        this.letter = letter;
        this.firstWord = firstWord;
        this.lastWord = lastWord;
    }
}
