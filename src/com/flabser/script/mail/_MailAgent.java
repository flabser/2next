package com.flabser.script.mail;

import java.util.ArrayList;
import com.flabser.script._Session;

public class _MailAgent {

    _Session session;

    public _MailAgent(_Session session) {
        this.session = session;
    }

    public ArrayList<String> organizeRecipients(ArrayList<String> recipients) {
        ArrayList<String> result = new ArrayList<>();

        for (String recipient : recipients) {
            if (recipient.startsWith("[") && recipient.endsWith("]")) {
                /*ArrayList<_Employer> ussersByRoles = session.getStructure().getAppUsersByRoles(recipient);
                for (_Employer ussersByRole : ussersByRoles) {
                    result.add(ussersByRole.getEmail());
                }*/
            } else {
                result.add(recipient);
            }
        }
        return result;
    }

    public boolean sendMail(ArrayList<String> recipients, _Memo m) {
		return false;

      /*  Memo memo = new Memo(Environment.defaultSender, organizeRecipients(recipients), m.msubject, m.body);
        return memo.send();*/
    }

   


    public boolean sendMailAfter(ArrayList<String> recipients, String subj, String body) {
		return false;
      /*  final Memo memo = new Memo(Environment.defaultSender, organizeRecipients(recipients), subj, body);
        RunnableFuture<Boolean> f = new FutureTask<>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return memo.send();
            }
        });
        new Thread(f).start();
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            Environment.logger.errorLogEntry(e);
            return false;
        }*/
    }

    public boolean sendMailAfter(String sender, String personal, ArrayList<String> recipients, String subj, String body) {
		return false;
        /*final Memo memo = new kz.pchelka.reminder.Memo(sender, personal, organizeRecipients(recipients), subj, body);
        RunnableFuture<Boolean> f = new FutureTask<>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return memo.send();
            }
        });
        new Thread(f).start();
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            Environment.logger.errorLogEntry(e);
            return false;
        }*/
    }

    

}
