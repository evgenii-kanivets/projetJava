package command;

import entity.RssFeed;
import storage.RssFeedStorage;

import java.util.Arrays;

/**
 * Util class that handles all the commands entered from command line.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class CommandHandler {

    public static final String CMD_HELP = "help";
    public static final String CMD_EXIT = "exit";

    public static final String CMD_ADD = "add";
    public static final String CMD_LIST = "list";
    public static final String CMD_REMOVE = "remove";

    private boolean shouldExit;
    private RssFeedStorage rssFeedStorage;

    public CommandHandler() {
        rssFeedStorage = new RssFeedStorage();
    }

    /**
     * @param command that were entered from CLI
     * @param command that were entered from CLI
     * @return true if command was handled
     */
    public boolean handleCommand(String command) {
        if (command == null) {
            return false;
        }

        String[] words = command.split(" ");

        String[] params = null;
        if (words.length > 1) {
            params = Arrays.copyOfRange(words, 1, words.length);
        }

        switch (words[0]) {
            case CMD_HELP:
                return help(params);

            case CMD_EXIT:
                return exit(params);

            case CMD_ADD:
                return add(params);

            case CMD_LIST:
                return list(params);

            case CMD_REMOVE:
                return remove(params);

            default:
                return false;
        }
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    private boolean help(String[] params) {
        System.out.println("Here are options:\n" +
                " - add RSS link with <name> <link> <period of check in second> (optional). Example: 'add rss1 http://google.com'\n" +
                " - list added RSS links. Example: 'list'\n" +
                " - remove RSS link by name. Example: 'remove rss1'");
        return true;
    }

    private boolean exit(String[] params) {
        shouldExit = true;
        return true;
    }

    private boolean add(String[] params) {
        if (params == null || params.length < 2) {
            return false;
        }

        String name = params[0];
        String url = params[1];
        int period = RssFeed.DEFAULT_PERIOD;

        if (params.length > 2) {
            try {
                period = Integer.parseInt(params[2]);
            } catch (Exception e) {
                return false;
            }
        }

        RssFeed rssFeed = new RssFeed(name, url, period);
        rssFeedStorage.add(rssFeed);

        return true;
    }

    private boolean list(String[] params) {
        for (RssFeed rssFeed : rssFeedStorage.getAll()) {
            System.out.println(rssFeed);
        }
        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean remove(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        String name = params[0];
        RssFeed rssFeedToRemove = null;
        for (RssFeed rssFeed : rssFeedStorage.getAll()) {
            if (name.equals(rssFeed.getName())) {
                rssFeedToRemove = rssFeed;
            }
        }

        if (rssFeedToRemove == null) {
            return false;
        } else {
            return rssFeedStorage.remove(rssFeedToRemove);
        }
    }

}