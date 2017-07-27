package org.telegram.telegrambots;

enum MorraCinese {
    SASSO("Sasso"), CARTA("Carta"), FORBICE("Forbice");

    private final String descrizione;

    private MorraCinese(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public static MorraCinese fromString(String descrizione) {
        if (descrizione != null) {
            for (MorraCinese sesso : MorraCinese.values()) {
                if (descrizione.equalsIgnoreCase(sesso.descrizione)) {
                    return sesso;
                }
            }
        }
        return null;
    }
}
