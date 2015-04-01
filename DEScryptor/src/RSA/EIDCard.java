package RSA;

//import be.belgium.eid.BEID_ReaderContext;
//import be.belgium.eid.BEID_ReaderSet;

public class EIDCard {

	private static String osName = System.getProperty("os.name");

	public static void main(String argv[]) throws Exception {
		/*
		if (-1 != osName.indexOf("Windows")) {
			System.out.println("[Info] Windows system!!");
			System.loadLibrary("beid35JavaWrapper-win.jar");
		} else {
			System.loadLibrary("beid35JavaWrapper-win");
		}

		BEID_ReaderSet.initSDK();
		// access the eID card here
		
		long nrReaders = BEID_ReaderSet.instance().readerCount();
		String[] readerList = BEID_ReaderSet.instance().readerList();
		for (int readerIdx = 0; readerIdx < nrReaders; readerIdx++) {
			BEID_ReaderContext readerContext = BEID_ReaderSet.instance()
					.getReaderByName(readerList[readerIdx]);
			boolean bCardPresent = readerContext.isCardPresent();
		}

		BEID_ReaderSet.releaseSDK();
		*/
	}
}
