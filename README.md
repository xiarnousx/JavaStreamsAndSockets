# JavaStreamsAndSockets
This project demonstrate the usage of various I/O Streams and Integration with Server Socket

<h2>Server.java &amp; ServerTest.java</h2>
<p>
	The engine for this simple application, the Server class is responsible for openning server socket defined in the properties file.
	Also, it extend the Thread class to make the server accept and respond to streams on a different thread than that of the main thread of ServerDriver.java
</p>
<b>The below test code snnipet found in ServerTest.java tests the mail box of a particular user to have 6 emails in inbox folder and 0 emails in the sent emails</b><br/>
_This should be adjusted while performing the tests_

```Java

			
	Object obj = oin.readObject();
	if (obj instanceof SyncInbox)
	{
		SyncInbox mail = (SyncInbox) obj;
		assertEquals("shld be 6", 6, ((ArrayList<Email>)mail.inbox.get(SyncInbox.INBOX_KEY)).size() );
		assertEquals("shld be 0", 0, ((ArrayList<Email>)mail.inbox.get(SyncInbox.SENT_KEY)).size() );
	}
```
