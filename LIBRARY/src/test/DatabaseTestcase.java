package test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

import code.Database;
import code.*;

class DatabaseTestcase {

    @Test
    void checkNameTest() throws SQLException, IOException{
        Database db = Database.getInstance();
        Boolean ret = db.checkExistMember("45645456", "456456");
        assertEquals(false, ret);}

    @Test
    void checkNameTest2() throws SQLException, IOException{
        Database db = Database.getInstance();
        Boolean ret = db.checkExistMember("123", "123");
        assertEquals(true, ret);}
}