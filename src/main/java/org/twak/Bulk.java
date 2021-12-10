package org.twak;

//import com.microsoft.graph.models.extensions.User;

import java.io.IOException;
import java.util.Properties;

/**
 * Bulk permissions for OneDrive
 */
public class Bulk {
	public static void main(String[] args) {

		new Graph (new Authentication("0xdeadbeef"), // "Application (client) ID " from ms graph https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2
				"cw2_results_2021",
				"This file on OneDrive contains your 2811 coursework 2 feedback. Download and unzip the folder and open in a browser such as Chrome.",
				"leeds.ac.uk" );
	}
}