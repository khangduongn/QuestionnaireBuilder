import java.io.*;

//Code is taken and altered from the StudentRepo VehicleCityVersion project (SerializationHelper.java)
//Referenced SE310 - Introduction to JAVA - Serialization video from Week 1
public class Output {

	public static <T> void serialize(Class<T> type, Object object, String directoryPath, String filename) {

		// validate the path to ensure that the directory exists or create directory if
		// it doesn't exist
		File directory = new File(directoryPath);

		if (!directory.exists()) {
			boolean isCreated = directory.mkdirs();
			if (!isCreated) {
				System.out.println("Unable to create the directory to save the file. Saving unsuccessful.");
				return;
			}
		} else if (!directory.isDirectory()) {
			System.out.println("The provided path to save the file is not a directory. Saving unsuccessful.");
			return;
		}

		// try to save the serialized survey
		try {
			// open streams
			FileOutputStream fos = new FileOutputStream(directoryPath + filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			// write the object
			oos.writeObject(type.cast(object));

			// close streams
			oos.close();
			fos.close();

			System.out.println("Successfully saved.");
		} catch (IOException e) {
			// if an error occurs, print error message
			System.out.println("An error has occurred when attempting to save the file. Saving unsuccessful.");
		}
		System.out.println();
	}

	public static <T> T deserialize(Class<T> type, String path) {

		// check that the file is at the path, exists, and is a valid file
		File file = new File(path);

		// if file doesn't exist or is not a file
		if (!file.exists() || !file.isFile()) {

			// then, print error message and return null
			System.out.println("Unable to find the file. It either doesn't exist or is invalid. Loading unsuccessful.");
			return null;
		}

		// this generic object holds the deserialized object
		T deserializedObject = null;

		try {
			// open streams to read
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);

			// read and cast to specific type
			deserializedObject = type.cast(ois.readObject());

			// close streams
			ois.close();
			fis.close();

		} catch (IOException | ClassNotFoundException e) {
			// if error occurs, print error message and return null
			System.out.println("An error has occurred when loading the file. Loading unsuccessful.");
		}

		return deserializedObject;
	}

}
