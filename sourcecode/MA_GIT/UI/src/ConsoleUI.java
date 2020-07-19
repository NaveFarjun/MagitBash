import MagitExceptions.*;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UserInterfaceForMagit {
    private RepositoryManager repositoryManager = new RepositoryManager();

    @Override
    public void changeRepository() {
        System.out.println("please enter the path of the repository");
        String path = ConsoleUtils.ReadLine();
        try {
            repositoryManager.ChangeRepository(path);
            System.out.println("Repository has been changed successfully");
        } catch (IOException | ParseException | RepositorySameToCurrentRepositoryException | RepositoryDoesnotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showStatus() {
        List<List<String>> listOfDelta;
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            listOfDelta = repositoryManager.ShowStatus();
            System.out.println("Repository name: " + repositoryManager.GetCurrentRepository().getName() + "\nLocation: " + repositoryManager.GetCurrentRepository().GetLocation() + "\nCurrent user: " + repositoryManager.GetUser() + System.lineSeparator());
            if (!listOfDelta.get(0).isEmpty()) {
                System.out.println("the new files are:");
                listOfDelta.get(0).stream().forEach(value -> System.out.println(value));
            } else {
                System.out.println("nothing added");
            }
            System.out.println("=========================================");
            if (!listOfDelta.get(1).isEmpty()) {
                System.out.println("the files that changed are:");
                listOfDelta.get(1).stream().forEach(value -> System.out.println(value));
            } else {
                System.out.println("nothing changed");
            }
            System.out.println("=========================================");
            if (!listOfDelta.get(2).isEmpty()) {
                System.out.println("the files that deleted are:");
                listOfDelta.get(2).stream().forEach(value -> System.out.println(value));
            } else {
                System.out.println("nothing deleted");
            }
        } catch (CommitException e) {
            System.out.println(e.getMessage());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeCommit() {
        try {
            repositoryManager.IsCurrentRepositoryInitialize();
            System.out.println("please enter the commit message");
            String message = ConsoleUtils.ReadLine();
            repositoryManager.MakeCommit(message);
            System.out.println("Commit added successfully");

        } catch (RepositoryDoesnotExistException | CommitException e) {
            System.out.println(e.getMessage());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeNewBranch() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            System.out.println("please enter a new branch name");
            String name = ConsoleUtils.ReadLine();
            repositoryManager.CreateNewBranch(name);
            System.out.println("Do you want to make checkout ? (true / false)");
            boolean choice = ConsoleUtils.ReadBoolean();
            if (choice) {
                if (!repositoryManager.HasOpenChanges()) {
                    repositoryManager.CheckOut(name);
                } else {
                    System.out.println("You have open changes so you cannot do checkout");
                }
            }
            System.out.println("Branch \"" + name +"\" has been created successfully");
        } catch (CommitException | BranchNameIsAllreadyExistException | BranchIsAllReadyOnWCException | BranchDoesNotExistException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBranch() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            System.out.println("please enter branch's name to delete");
            String name = ConsoleUtils.ReadLine();
            repositoryManager.DeleteBranch(name);
            System.out.println("Branch \"" + name +"\" has been deleted ");
        } catch (CommitException | HeadBranchDeletedExcption |
                BranchDoesNotExistException | BranchFileDoesNotExistInFolderException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void checkOut() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            if (repositoryManager.HasOpenChanges()) {
                System.out.println("You have open changes, Do you want to continue?");
                boolean choice = ConsoleUtils.ReadBoolean();
                if (!choice) {
                    return;
                }
            }
            System.out.println("please enter branch name to checkout:");
            String name = ConsoleUtils.ReadLine();
            repositoryManager.CheckOut(name);
            System.out.println("Checkout completed successfully");
        } catch (BranchDoesNotExistException | BranchIsAllReadyOnWCException |
                CommitException | IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void changeUser() {
        System.out.println("enter user name:");
        String name = ConsoleUtils.ReadLine();
        User user = new User(name);
        repositoryManager.ChangeUser(user);
        System.out.println("User was changed to: "+ name);
    }

    @Override
    public void showAllBranches() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            List<BranchDetails> list = repositoryManager.ShowBranches();
            list.forEach(System.out::println);
        } catch (CommitException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showActiveBranchHistory() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            List<CommitDetails> list = repositoryManager.ShowActiveBranchHistory();
            list.forEach(System.out::println);
        } catch (CommitException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void initRepository() {
        System.out.println("enter repository path");
        String path = ConsoleUtils.ReadLine();
        System.out.println("enter repository name");
        String name = ConsoleUtils.ReadLine();
        try {
            repositoryManager.BonusInit(name, path);
            System.out.println("Repository: " +name+" in: \"" + path +"\" has been created successfully");
        } catch (FileAlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAllCommitFiles() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            List<FileDetails> commitFile = repositoryManager.ShowAllCommitFiles();
            for (FileDetails fd : commitFile) {
                System.out.println(fd);
            }
        } catch (CommitException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetHeadBranch() {
        try {
            repositoryManager.IsRepositoryHasAtLeastOneCommit();
            List<FileDetails> list;
            System.out.println("please insert commit SHA-1 to reset for Head branch:");
            SHA1 sh1 = new SHA1(ConsoleUtils.ReadLine());
            if (repositoryManager.HasOpenChanges()) {
                System.out.println("You have open changes. Do you want to continue?  (true / false)");
                if (!ConsoleUtils.ReadBoolean()) {
                    return;
                }
            }
            list = repositoryManager.ResetHeadBranch(sh1);
            list.forEach(System.out::println);
            System.out.println("Head branch has been changed to: " +sh1.getSh1()+" successfully");
        } catch (CommitException e) {
            System.out.println(e.getMessage());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initRepositoryFromXMLFile() {
        String path;
        System.out.println("please enter xml path");
        path = ConsoleUtils.ReadLine();
        try {
            List<String> errors = repositoryManager.CheckXml(path);
            if (!errors.isEmpty()) {
                errors.forEach(System.out::println);
            } else {
                if (repositoryManager.IsRepositoryExist(repositoryManager.GetMagitRepository().getLocation())) {
                    System.out.println("You have already repository on this path: \"" + repositoryManager.GetMagitRepository().getLocation() + "\". Do you want to override it?");
                    if (ConsoleUtils.ReadBoolean()) {
                        repositoryManager.LoadXML();
                        System.out.println("XML file loaded successfully");
                    }
                } else {
                    repositoryManager.LoadXML();
                    System.out.println("XML file loaded successfully");
                }
            }
        } catch (NoSuchMethodException | FileNotFoundException | IllegalAccessException | InvocationTargetException | JAXBException e) {
            e.printStackTrace();
        } catch (BranchIsAllReadyOnWCException | BranchDoesNotExistException | XMLException | RepositoryAllreadyExistException e) {
            System.out.println(e.getMessage());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ExportToXml() {
        String path;
        System.out.println("Please enter xml path");
        path = ConsoleUtils.ReadLine();
        if (repositoryManager.GetCurrentRepository() != null) {
            try {
                repositoryManager.ExportRepositoryToXML(path);
                System.out.println("Export executed successfully");
            } catch (XMLException | RepositoryDoesnotExistException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Error: You have to initialize repository first");
        }

    }

    @Override
    public void Run() {
        PrintMenu();
        int i = ConsoleUtils.ReadIntInRange(-1, 13);
        while (i != -1) {
            switch (i) {
                case 1:
                    initRepository();
                    break;
                case 2:
                    changeRepository();
                    break;
                case 3:
                    showStatus();
                    break;
                case 4:
                    makeCommit();
                    break;
                case 5:
                    makeNewBranch();
                    break;
                case 6:
                    checkOut();
                    break;
                case 7:
                    deleteBranch();
                    break;
                case 8:
                    showActiveBranchHistory();
                    break;
                case 9:
                    showAllBranches();
                    break;
                case 10:
                    showAllCommitFiles();
                    break;
                case 11:
                    resetHeadBranch();
                    break;
                case 12:
                    initRepositoryFromXMLFile();
                    break;
                case 13:
                    ExportToXml();
                    break;
                case 0:
                    changeUser();
                    break;
            }
            PrintMenu();
            i = ConsoleUtils.ReadIntInRange(-1, 13);
        }

    }

    private void PrintMenu() {
        String repoName = repositoryManager.GetCurrentRepository()==null?"None" :repositoryManager.GetCurrentRepository().getName();
        System.out.println("==================================================================");
        System.out.println(" Magit Menu     User: " + repositoryManager.GetUser() + "     Repository: " + repoName);
        System.out.println(" 0. Change user");
        System.out.println(" 1. init new repository");
        System.out.println(" 2. Switch repository");
        System.out.println(" 3. Working copy status");
        System.out.println(" 4. Commit");
        System.out.println(" 5. Create new branch");
        System.out.println(" 6. Checkout branch");
        System.out.println(" 7. Delete Branch");
        System.out.println(" 8. show previous commits");
        System.out.println(" 9. List available branches");
        System.out.println("10. Show current commit file system information");
        System.out.println("11. Reset branch");
        System.out.println("12. Load repository from XML file");
        System.out.println("13. Export repository to XML file");
        System.out.println("-1. Exit");

    }
}
