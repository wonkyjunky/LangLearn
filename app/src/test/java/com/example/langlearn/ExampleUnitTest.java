package com.example.langlearn;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void usernameSpecial_isCorrect() {
        String username = "Reval@";
        assertEquals(true, CredentialActivity.checkuserName_if_it_has_special_character(username));
    }
    @Test
    public void usernameTenCharacter_isCorrect() {
        String username = "Revalfdjakfdjkafda";
        assertEquals(true, CredentialActivity.checkuserName_if_it_has_morethan_ten_characters(username));
    }
    @Test
    public void usernameUpperCase_isCorrect() {
        String username = "reval";
        assertNotEquals(true, CredentialActivity.checkuserName_upperCharacter(username));
    }
}

