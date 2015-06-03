package com.flabser.script.mail;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import com.flabser.mail.Memo;
import com.flabser.script._Session;
import com.flabser.env.*;

public class _MailAgent {

	_Session session;

	public _MailAgent(_Session session) {
		this.session = session;
	}

	public boolean sendMail(ArrayList<String> recipients, String subj, String body, boolean async) {
		Memo memo = new Memo(Environment.defaultSender, recipients, subj, body);
		if (async) {
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
			}
		} else {
			return memo.send();
		}
	}

}
