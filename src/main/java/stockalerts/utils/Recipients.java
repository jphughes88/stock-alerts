package stockalerts.utils;

public enum Recipients {

    ALL_RECIPIENTS("johnnysps5updates@gmail.com, martynwalsh1@hotmail.co.uk, alexcoglan@gmail.com, dave.russell7933@live.com"),
    JOHNNY("johnnysps5updates@gmail.com");

    private final String emailAddresses;

    Recipients(String emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public String getEmails() {
        return emailAddresses;
    }
}
