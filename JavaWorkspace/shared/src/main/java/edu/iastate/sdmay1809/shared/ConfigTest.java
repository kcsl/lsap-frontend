package edu.iastate.sdmay1809.shared;

public class ConfigTest {

	public static void main(String args[]) {
		String[] args2 = {
				"-Dthis=that",
				"-Dthis2=that2",
				"-Dthis3.this4=that5",
				"-Dthis5.1=one",
				"-Dthis5.2=two",
		};
		
		Config.Builder<Config> b = Config.builder(args2);
		b.build();
	}
}
