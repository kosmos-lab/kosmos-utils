
import de.kosmos_lab.utils.HashFunctions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HashFunctionsTest {
    
    
    @Test
    public void testSHA3() {
    
        Assert.assertEquals(HashFunctions.getSHA3("DFKI-CPS-BAALL"), "be9473670d3df1301e05c02a882bc40faaad2255c29cfe2a061e859c50051e9a", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSHA3("BAALL"), "1774292ab877503a1b0189b7a0f13ddad96460727d9eeac945564e58bddb8106", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSHA3(""), "a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSHA3("DFKI"), "f194e41b2c3837d009d71b14aa983c249be4c7fe5fd9c3cccea0c8bd5a68cd1f", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSHA3("CPS"), "39a7651e51d9afab1d643bc203e564cacad4457d172a2603fb1f129f7a889584", "SHA3-256 did NOT match the correct value");
    }
    
    @Test
    public void testSalted() {
        Assert.assertEquals(HashFunctions.getSaltedHash("DFKI", "CPS"), "3074a909d07d813a73024d82bd0ae6f4d96546874f6b69b3d5b39e99e0866cc1", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("BAALL", "CPS"), "027dda76858bb544203d1ca330c98e14489a8586bf8c2c98299e75e1c153b87c", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("DFKI", "BAALL"), "783d8ebd6714340bcc2fafb711004b20734c8dab37a8b069d8e8a8c960899d35", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("CPS", "BAALL"), "3db11924edcbede431028539cea354c025f4a15e8190f35b8ed2ded9640317a7", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("", "BAALL"), "edb4f84da785b5c7660196c2ef1beb63c4449bdb924df7644c883edfb2dd9585", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("BAALL", ""), "da73e8223a94422cc3cbb25403f3b3eb6a4b05026348ca13476aff2332aa8ae1", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedHash("", ""), "42012470e20e90036b3098da71e1056ce0e561031bc41fa47faa1d1269f93a2e", "SHA3-256 did NOT match the correct value");
        
    }
    
    @Test
    public void testSaltedAndPeppered() {
        Assert.assertEquals(HashFunctions.getSaltedAndPepperdHash("DFKI", "CPS", "BAALL"), "d5ffe4d711a7fc6dbb2f06c700269b4b24ba1e8c4a7f7fd1e0a14fc4a7efecca", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedAndPepperdHash("BAALL", "CPS", "DFKI"), "750e14609878e6a8c3b6f796417fc1f40ae54a87d2013f1134346c363b5632c3", "SHA3-256 did NOT match the correct value");
        Assert.assertEquals(HashFunctions.getSaltedAndPepperdHash("DFKI", "BAALL", "CPS"), "d2399d7082e4839552ee9343bffe29779fcdf88d45af09a08bd2bc55cccc7000", "SHA3-256 did NOT match the correct value");
        
    }
}