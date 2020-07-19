import MagitExceptions.CommitException;

public interface UserInterfaceForMagit {
    void changeRepository();
    void showStatus();
    void makeCommit();
    void makeNewBranch() throws CommitException;
    void deleteBranch();
    void checkOut();
    void changeUser();
    void showAllBranches();
    void showActiveBranchHistory();
    void initRepository();
    void Run();
    void showAllCommitFiles();
    void resetHeadBranch();
    void initRepositoryFromXMLFile();
    void ExportToXml();
}
