package com.crowsnet.rbhelper.v2;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by CrowsNet on 26.11.2017.
 */
public class Tasks {

    private static WebEngine webEngine;
    private static String user, pass, character;
    private static TextField hintLabel;

    public static void init(WebEngine webEngine, String user, String pass, String character, TextField hintLabel) {
        Tasks.webEngine = webEngine;
        Tasks.user = user;
        Tasks.pass = pass;
        Tasks.character = character;
        Tasks.hintLabel = hintLabel;
    }

    public static class LogoutTask implements Chain.Task {

        @Override
        public boolean run() throws Exception{
            if (exec("document.querySelector('#login')") == null) {
                if (exec("document.querySelector('#logout')") != null)
                    exec("document.querySelector('#logout').click()");

                return false;
            } else
                return true;
        }

        @Override
        public void onSuccess() {
            setMessage("Prepared login");
        }

        @Override
        public void onFail() {
            setMessage("Failed to prepare login");
        }
    }

    public static class LoginTask implements Chain.Task {

        @Override
        public boolean run() throws Exception {
            if (exec("document.querySelector('#login')") != null) {
                exec(
                        "document.querySelector('#lun').value='" + user + "';" +
                                "document.querySelector('#lpw').value='" + pass + "';" +
                                "document.querySelector('#login').click();"
                );
                return false;
            } else {
                return exec("document.querySelector('#logout')") != null;
            }
        }

        @Override
        public void onSuccess() {
            setMessage("Logged in as " + user);
        }

        @Override
        public void onFail() {
            setMessage("Can't log in as " + user);
        }
    }

    public static class SelectionTask implements Chain.Task {

        @Override
        public boolean run() throws Exception {
            exec("document.querySelector('#acc5 .achd').click();" +
                    "var found = false;" +
                    "var chars = document.querySelectorAll('#acc5 .acbd .cbtn');" +
                    "for (var c = 0; c < chars.length; c++) {" +
                    "if (chars[c].innerText.indexOf('" + character + " ') != -1) {" +
                    "   chars[c].click();" +
                    "   found = true;" +
                    "}" +
                    "}" +
                    "if (!found) {" +
                    "   throw 'Char not found';" +
                    "}");
            return true;
        }

        @Override
        public void onSuccess() {
            setMessage("Selected " + character + "@" + user);
        }

        @Override
        public void onFail() {
            setMessage("Can't select " + character + "@" + user);
        }
    }

    public static class RebirthTask implements Chain.Task {

        private int rbNow;
        private boolean passRb;

        @Override
        public boolean run() throws Exception {
            if (passRb) {
                for (int c = 0; c < 10; c++) {
                    exec("document.querySelector('#chrspace .bt_iii').click()");
                }
                exec("document.querySelector('#ap_bt').click()");

                boolean success = Integer.parseInt((String) exec("document.querySelector('#pts').innerText")) < 1;
                if (success) {
                    rbNow = Integer.parseInt((String) exec("document.querySelector('#c_rb').innerText"));
                }
                return success;
            } else {
                exec("document.querySelector('#rb_bt').click()");

                passRb = Integer.parseInt((String) exec("document.querySelector('#pts').innerText")) > 0;
                return false;
            }
        }

        @Override
        public void onSuccess() {
            setMessage("Successfully rebirthed " + character + "@" + user + ", RB=" + rbNow);
            passRb = false;
        }

        @Override
        public void onFail() {
            setMessage("Rebirth of " + character + "@" + user + " failed");
        }
    }

    public static class Wait110Task implements Chain.AsyncTask {

        private boolean isFail;
        private long time;

        @Override
        public boolean run() throws Exception {
            if (time == 0)
                time = System.currentTimeMillis();

            if (!isFail) {
                setMessage("Passing control of " + character + "@" + user + " to mBotBridge");

                String result = execBridge(character);
                setMessage(result);

                isFail = result.startsWith("err_");
            }

            return !isFail;
        }

        @Override
        public void onSuccess() {
            long took = System.currentTimeMillis() - time;
            setMessage(character + "@" + user + " leveled to 110 in " + took + " millis");
            time = 0;
        }

        @Override
        public void onFail() {
            setMessage("Leveling " + character + "@" + user + "to 110 failed");
        }
    }

    private static Object exec(String query) throws Exception {
        return webEngine.executeScript(query);
    }

    private static String execBridge(String character) throws IOException {
        return execEx("./misc/mBotBridgeV2.exe", character);
    }

    private static String execEx(String ... params) throws IOException {
        Runtime rt = Runtime.getRuntime();

        Process au3Process = rt.exec(params);
        ProcessWatcher watcher = new ProcessWatcher(au3Process);

        Thread closeChildThread = new Thread() {
            public void run() {
                au3Process.destroy();
            }
        };
        rt.addShutdownHook(closeChildThread);

        try {
            au3Process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return watcher.getResult();
    }

    private static void setMessage(String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                hintLabel.setText(msg);
            }
        });
        System.out.println(msg);
    }

    private static class ProcessWatcher extends Thread {
        private Process p;
        private volatile String result;

        public ProcessWatcher(Process p) {
            this.p = p;
            this.setDaemon(true);
            this.start();
        }

        public void run() {
            try {
                Scanner scanner = new Scanner(p.getInputStream());

                while (true) {
                    String line = scanner.nextLine();
                    if (line.startsWith("err_") || line.startsWith("ok_")) {
                        result = line;
                        break;
                    } else {
                        setMessage(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getResult() {
            return result;
        }
    }
}
