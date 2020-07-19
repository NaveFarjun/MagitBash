import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;


public class RepositoryManagerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @Test
    public void testInitRepository_sanity() throws Exception {
        FileUtils.deleteDirectory("C:\\repo33");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("Test", "C:\\repo33");
        Assert.assertTrue(new File("C:\\repo33\\.magit").isDirectory());
    }

    @Test
    public void deleteDirectory() throws Exception {
        new File("C:\\repo6").mkdirs();
        new File("C:\\repo6\\folder2").mkdirs();
        FileUtils.WriteToFile("hey", "C:\\repo6\\file1.txt");
        FileUtils.WriteToFile("nave", "C:\\repo6\\folder2\\file2.txt");
        FileUtils.deleteDirectory("C:\\repo6");
    }

    @Test
    public void testInitRepository_existsException() throws Exception {
        expectedException.expect(FileAlreadyExistsException.class);
        expectedException.expectMessage("The directory: C:\\repo33 already exists");

        FileUtils.deleteDirectory("C:\\repo33");
        new File("C:\\repo33").mkdirs();
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("Test", "C:\\repo33");
    }

    @Test
    public void testtest() throws Exception {
        FileUtils.deleteDirectory("C:\\repo33");
        FileUtils.deleteDirectory("C:\\repo34");

        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("Test", "C:\\repo33");

        FileUtils.WriteToFile("test input", "C:\\repo33\\file1.txt");
        repositoryManager.MakeCommit("commit message 33 1");

        new File("C:\\repo33\\folder1").mkdirs();
        FileUtils.WriteToFile("test input2", "C:\\repo33\\folder1\\file2.txt");
        repositoryManager.MakeCommit("commit message 33 2");

        repositoryManager.BonusInit("Test2", "C:\\repo34");
        FileUtils.WriteToFile("test input", "C:\\repo34\\file1.txt");
        repositoryManager.MakeCommit("commit message 34 1");

        new File("C:\\repo34\\folder1").mkdirs();
        FileUtils.WriteToFile("test input2", "C:\\repo34\\folder1\\file2.txt");
        repositoryManager.MakeCommit("commit message 34 2");

        repositoryManager.ShowActiveBranchHistory();
        repositoryManager.ChangeRepository("C:\\repo33");
        repositoryManager.ShowActiveBranchHistory();
    }

/*    @Test
    public void testByDan() throws Exception {
        FileUtils.deleteDirectory("C:\\repo1");
        Files.deleteIfExists(Paths.get("C:\\repo1"));
        Files.deleteIfExists(new File("C:\\repo1").toPath());

        RepositoryManager repositoryManager = new RepositoryManager();
        List<String> errors = repositoryManager.CheckXml("C:\\Users\\Dan Beladev\\Desktop\\New folder\\correct\\ex1-large.xml");
        Assert.assertTrue(errors.isEmpty());
        repositoryManager.LoadXML();
        for (File file : new File("C:\\repo1").listFiles()) {
            if (file.getName().equals(".magit")) {
                continue;
            }
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file.toPath().toString());
            } else {
                file.delete();
            }
        }
        FileUtils.WriteToFile("content", "C:\\repo1\\file1.txt");
        repositoryManager.MakeCommit("commit 1");
    }*/

    @Test
    public void InitBonus() throws Exception {
        FileUtils.deleteDirectory("C:\\repo1");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("dan", "C:\\repo1");
    }

    @Test
    public void InitplusCommit() throws Exception {
        FileUtils.deleteDirectory("C:\\repo1");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("dan", "C:\\repo1");
        FileUtils.WriteToFile("blabla", "C:\\repo1\\file1.txt");
        repositoryManager.MakeCommit("first commit");
        FileUtils.WriteToFile("heyy", "C:\\repo1\\file2.txt");
        new File("C:\\repo1\\folder1").mkdirs();
        FileUtils.WriteToFile("dubi", "C:\\repo1\\folder1\\file3.txt");
        repositoryManager.MakeCommit("second commit");
    }

    @Test
    public void BuildDirectory() throws Exception {
        FileUtils.deleteDirectory("C:\\repo1");
        FileUtils.BuildTestDirectory("C:\\repo1");
        FileUtils.DoChangesTest("C:\\repo1");
    }

    @Test
    public void ResetBranchTest() throws Exception {
        FileUtils.deleteDirectory("C:\\repo1");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("Shoko", "C:\\repo1");
        FileUtils.BuildTestDirectory("C:\\repo1");
        repositoryManager.MakeCommit("first");
        List<FileDetails> l1 = repositoryManager.ShowAllCommitFiles();
        FileUtils.DoChangesTest("C:\\repo1");
        repositoryManager.MakeCommit("second");
        System.out.println(repositoryManager.GetCurrentRepository().getActiveBranch().getName());
        System.out.println(repositoryManager.GetCurrentRepository().getActiveBranch().getCommitSH1());
        List<FileDetails> l2 = repositoryManager.ResetHeadBranch(repositoryManager.GetCurrentRepository().getCommitMap().get(repositoryManager.GetCurrentRepository().getActiveBranch().getCommitSH1()).getPrevCommits().get(0));
        System.out.println(repositoryManager.GetCurrentRepository().getActiveBranch().getName());
        System.out.println(repositoryManager.GetCurrentRepository().getActiveBranch().getCommitSH1());
        Assert.assertTrue(l2.toString().equals(l1.toString()));
    }

    @Test
    public void commitFileInfoTest() throws Exception {
        FileUtils.deleteDirectory("C:\\repo2");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.BonusInit("Shoko", "C:\\repo2");
        FileUtils.BuildTestDirectory("C:\\repo2");
        repositoryManager.MakeCommit("first");
        repositoryManager.CreateNewBranch("first");
        List<FileDetails> list = repositoryManager.ShowAllCommitFiles();
        FileUtils.DoChangesTest("C:\\repo2");
        repositoryManager.MakeCommit("second");
        //System.out.println(repositoryManager.ShowAllCommitFiles());
        repositoryManager.CheckOut("first");
        List<FileDetails> list2 = repositoryManager.ShowAllCommitFiles();

        if (!list.toString().equals(list2.toString())) {
            throw new Exception("not equal information");
        }
    }

    @Test
    public void switchRepositories() throws Exception {

        FileUtils.deleteDirectory("C:\\repo1");
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.CheckXml("C:\\Users\\nave\\Desktop\\05. XML\\ex1-large.xml");
        repositoryManager.LoadXML();
        if (!new File("C:\\repo1").exists()) {
            throw new Exception("the folder C:\\repo1 is not exist");
        }

        FileUtils.deleteDirectory("C:\\repo2");
        repositoryManager.BonusInit("shoko", "C:\\repo2");
        FileUtils.BuildTestDirectory("C:\\repo2");
        repositoryManager.MakeCommit("commit 1 in repo2");
        System.out.println("Active Branch history in repo2: \n" + repositoryManager.ShowActiveBranchHistory());
        System.out.println("Current conmmit File information in repo2: \n" + repositoryManager.ShowAllCommitFiles());

        repositoryManager.ChangeRepository("C:\\repo1");
        System.out.println("Active Branch history in repo1: \n" + repositoryManager.ShowActiveBranchHistory());

        FileUtils.WriteToFile("new content", "C:\\repo1\\a.txt");
        repositoryManager.MakeCommit("commit 1 in repo1");
        System.out.println("Active Branch history in repo1: \n" + repositoryManager.ShowActiveBranchHistory());

        FileUtils.deleteDirectory("C:\\repo1\\fol1");
        FileUtils.deleteDirectory("C:\\repo1\\fol2");
        new File("C:\\repo1\\a.txt").delete();
        FileUtils.WriteToFile("new content", "C:\\repo1\\b.txt");
        repositoryManager.CreateNewBranch("dan");
        repositoryManager.MakeCommit("commit 2 in repo 1");
        System.out.println("Current commit File information in repo1 before checkout: \n" + repositoryManager.ShowAllCommitFiles());
        repositoryManager.CheckOut("dan");
        System.out.println("Current commit File information in repo1 after checkout: \n" + repositoryManager.ShowAllCommitFiles());
    }
    @Test
    public void xmls() throws Exception {
        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.CheckXml("C:\\Users\\nave\\Desktop\\05. XML\\ex1-large.xml");
        repositoryManager.LoadXML();
        repositoryManager.CheckOut("master");
        repositoryManager.CheckOut("test");

    }

}