package xezz.org.dbunit.export.options;

public enum PresetOption {
    DATABASE("db"), USERNAME("name"), PASSWORD("password"), HOSTNAME("host"), SCHEMA("schema"), HELP("help");
    private String value;

    public String getValue() {
        return value;
    }

    PresetOption(String value) {
        this.value = value;
    }
}