
package axirassa.overlord;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zanoccio.javakit.DirectStreamForwarder;
import axirassa.overlord.exceptions.OverlordException;
import axirassa.overlord.os.AbstractOverlordSystemSupport;
import axirassa.overlord.os.OverlordSystemSupport;

public class Overlord {
	private final static Logger log = LoggerFactory.getLogger(Overlord.class);


	public static void main (String[] parameters) throws IOException, OverlordException, InterruptedException {
		log.info("Starting Overlord Wrapper");
		log.info("Locating configuration");

		URL config = OverlordUtilities.findResource("axoverlord.cfg.xml");
		if (config == null) {
			log.error("Could not find axoverlord.cfg.xml");
			return;
		}

		NativeLibraryProvider nlp = new NativeLibraryProvider();

		nlp.provideLibrary("/libTerminal.jnilib");
		nlp.provideLibrary("/libTerminal.so");
		nlp.provideLibrary("/Terminal.dll");

		OverlordSystemSupport system = AbstractOverlordSystemSupport.getSystemSupport();

		CommandLine cli = new CommandLine(system.getJavaExecutable());
		String jarFile = OverlordUtilities.retrieveJarFile(config);
		if (jarFile != null) {
			cli.addArgument("-cp");
			cli.addArgument(jarFile);
		}

		cli.addArgument("-Djava.library.path=" + nlp.getLibraryPath());

		cli.addArgument("axirassa.overlord.OverlordMain");

		ArrayList<String> builtCli = cli.buildCommandLine();
		log.info("Forking overlord main: " + builtCli);
		String[] args = new String[builtCli.size()];
		builtCli.toArray(args);

		ProcessBuilder builder = new ProcessBuilder(builtCli);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		DirectStreamForwarder outputForwarder = new DirectStreamForwarder(process.getInputStream(), System.out);
		DirectStreamForwarder inputForwarder = new DirectStreamForwarder(System.in, process.getOutputStream());

		outputForwarder.start();
		inputForwarder.start();

		process.waitFor();
		outputForwarder.destroy();
		inputForwarder.destroy();

		return;
	}
}
