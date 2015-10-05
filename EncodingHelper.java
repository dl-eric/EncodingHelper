import edu.andover.elee.EncodingHelperParser;

/*
 * @author Eric Lee and Diana Ding
 */
public class EncodingHelper {

	private static final String inputArgument = "--input";
	private static final String inputArgument2 = "-i";
	private static final String outputArgument = "--output";
	private static final String outputArgument2 = "-o";

	private static int inputIndex;
	private static int outputIndex;
	private static int dataIndex;

	private static String data;

	public static void main(String[] args) {
		if (args.length == 0) {
			printUsage();

			System.exit(1);
		}

		boolean foundInput = false;
		for(int i = 0; i < args.length; i++) {
			if (args[i].equals(inputArgument) 
					|| args[i].equals(inputArgument2)) {
				inputIndex = i + 1;

				if(inputIndex >= args.length 
						|| !isValidInput(args[inputIndex])) {
					printUsage();
					System.exit(1);
				}
				foundInput = true;
				break;
			}
		}

		boolean foundOutput = false;
		for(int j = 0; j < args.length; j++) {
			if (args[j].equals(outputArgument) 
					|| args[j].equals(outputArgument2)) {
				outputIndex = j + 1;

				if(outputIndex >= args.length 
						|| !isValidOutput(args[outputIndex])) {
					printUsage();
					System.exit(1);
				}
				foundOutput = true;
				break;
			}
		}

		if(foundInput && foundOutput) {
			data = args[4];
			dataIndex = 4;
		} else if (foundInput) {
			data = args[2];
			dataIndex = 2;
		} else if (foundOutput) {
			data = args[2];
			dataIndex = 2;
			/*
			 * Infer user input
			 */
			EncodingHelperParser ehp;
			if(data.contains("U+")) {
				ehp = new EncodingHelperParser("codepoint", data);
			}
			else if(data.contains("\\x")) {
				ehp = new EncodingHelperParser("utf8", data);
			} else {
				ehp = new EncodingHelperParser("string", data);
			}
			if(args[outputIndex].equals("string")) {
				ehp.printString();
				System.exit(0);
			}
			if(args[outputIndex].equals("codepoint")) {
				ehp.printCodepoint();
				System.exit(0);
			}
			if(args[outputIndex].equals("summary")) {
				ehp.printSummary();
				System.exit(0);
			}
			if(args[outputIndex].equals("utf8")) {
				ehp.printUtf8();
				System.exit(0);
			}
		} else {

			data = args[0];
			dataIndex = 0;
			/*
			 * Infer user input
			 */
			if(data.contains("U+")) {
				EncodingHelperParser ehpCP = new EncodingHelperParser("codepoint", data);
				ehpCP.printSummary();
			}
			else if(data.contains("\\x")) {
				EncodingHelperParser ehpU = new EncodingHelperParser("utf8", data);
				ehpU.printSummary();
			} else {
				EncodingHelperParser ehpS = new EncodingHelperParser(data);
				ehpS.printSummaryFromCharArray();
			}
		}

		if(foundInput) {
			switch(args[inputIndex]) {
			case "string":
				EncodingHelperParser ehcpString
				= new EncodingHelperParser("string", data);
				if(foundOutput) {
					switch(args[outputIndex]) {
					case "string":
						ehcpString.printString();
						break;
					case "utf8":
						ehcpString.printUtf8();
						break;
					case "codepoint":
						ehcpString.printCodepoint();
						break;
					default:
						ehcpString.printSummary();
						break;
					}
				} 
				break;
			case "utf8":
				EncodingHelperParser ehcpUtf8 = new EncodingHelperParser("utf8",
						data);
				if(foundOutput) {
					switch(args[outputIndex]) {
					case "string":
						ehcpUtf8.printString();
						break;
					case "utf8":
						ehcpUtf8.printUtf8();
						break;
					case "codepoint":
						ehcpUtf8.printCodepoint();
						break;
					default:
						ehcpUtf8.printSummary();
						break;
					}
				} else {
					ehcpUtf8.printSummary();
				}
				break;
			case "codepoint":
				if(dataIndex + 1 < args.length) {
					for(int i = dataIndex + 1; i < args.length; i++) {
						data = data + " " + args[i];	
					}
				}

				EncodingHelperParser ehcpCodepoint
				= new EncodingHelperParser("codepoint", data);

				if(foundOutput) {
					switch(args[outputIndex]) {
					case "string":
						ehcpCodepoint.printString();
						break;
					case "utf8":
						ehcpCodepoint.printUtf8();
						break;
					case "codepoint":
						ehcpCodepoint.printCodepoint();
						break;
					default:
						ehcpCodepoint.printSummary();
						break;
					}
				} 

				break;
			default:
				EncodingHelperParser ehcp 
				= new EncodingHelperParser(data);
				if(foundOutput) {
					switch(args[outputIndex]) {
					case "string":
						ehcp.printString();
						break;
					case "utf8":
						ehcp.printUtf8();
						break;
					case "codepoint":
						ehcp.printCodepoint();
						break;
					default:
						ehcp.printSummary();
						break;
					}
				}
				break;
			}
		} else {
			if(foundOutput) {
				EncodingHelperParser ehcp = new EncodingHelperParser("string", data);
				switch(args[outputIndex]) {
				case "string":
					ehcp.printString();
					break;
				case "utf8":
					ehcp.printUtf8();
					break;
				case "codepoint":
					ehcp.printCodepoint();
					break;
				default:
					ehcp.printSummary();
					break;
				}
			}
		}
	}

	public static void printUsage() {
		//Usage
		System.out.print("usage: java EncodingHelper");
		System.out.println("\t[--input/-i string/utf8/codepoint]");
		System.out.println("\t\t\t\t[--output/-o string/utf8/codepoint/summary]");
		System.out.println("\t\t\t\t<args><data>");
		System.out.println("Note that utf8 input data must be closed by ' '");
	}

	public static boolean isValidInput(String input) {
		return input.equals("string") || input.equals("utf8") 
				|| input.equals("codepoint");
	}

	public static boolean isValidOutput(String output) {
		return output.equals("string") || output.equals("utf8") 
				|| output.equals("codepoint") || output.equals("summary");
	}
}
