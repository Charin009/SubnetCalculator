package main;

public class Calculator {

		// Input IP Address and Subnet Mask
		// IP Address & Default Address
		private int firstIP = 192;
		private int secondIP = 168;
		private int thirdIP = 0;
		private int fourthIP = 1;

		// Subnet Mask & Default Mask
		private int firstMask = 255;
		private int secondMask = 255;
		private int thirdMask = 255;
		private int fourthMask = 0;

		private String Message;

		/*******************************************/

		private int firstNet, secondNet, thirdNet, fourthNet;
		private int firstHost1, firstHost2, firstHost3, firstHost4;
		private int lastHost1, lastHost2, lastHost3, lastHost4;
		private int wildMask1, wildMask2, wildMask3, wildMask4;
		private int broadcast1, broadcast2, broadcast3, broadcast4;

		public void init() {
			// Calculate Network Address
			firstNet = firstMask & firstIP;
			secondNet = secondMask & secondIP;
			thirdNet = thirdMask & thirdIP;
			fourthNet = fourthMask & fourthIP;

			// Calculate First Address
			firstHost1 = firstNet;
			firstHost2 = secondNet;
			firstHost3 = thirdNet;
			firstHost4 = fourthNet + 1;

			// Wildcard Netmask
			wildMask1 = ~firstMask & 0xff;
			wildMask2 = ~secondMask & 0xff;
			wildMask3 = ~thirdMask & 0xff;
			wildMask4 = ~fourthMask & 0xff;

			// Calculate Broadcast Address
			broadcast1 = firstIP | wildMask1;
			broadcast2 = secondIP | wildMask2;
			broadcast3 = thirdIP | wildMask3;
			broadcast4 = fourthIP | wildMask4;

			// Calculate Last Address
			lastHost1 = broadcast1;
			lastHost2 = broadcast2;
			lastHost3 = broadcast3;
			lastHost4 = broadcast4 - 1;
		}

		// "192.168.0.1"
		public void setIP(String ip) {
			String[] oct = new String[5];
			oct = ip.split("\\.");
			firstIP = (int) Math.abs(Double.parseDouble(oct[0]));
			secondIP = (int) Math.abs(Double.parseDouble(oct[1]));
			thirdIP = (int) Math.abs(Double.parseDouble(oct[2]));
			fourthIP = (int) Math.abs(Double.parseDouble(oct[3]));

		}

		public void setSubnet(String subnet) {
			if (Message != "That isn't a valid subnet!") {
				Message = "Results!";
			}
			String[] oct = subnet.split("\\.");
			firstMask = (int) Math.abs(Double.parseDouble(oct[0]));
			secondMask = (int) Math.abs(Double.parseDouble(oct[1]));
			thirdMask = (int) Math.abs(Double.parseDouble(oct[2]));
			fourthMask = (int) Math.abs(Double.parseDouble(oct[3]));

			if (firstMask < 128 && firstMask != 0) {
				firstMask = 0;
				Message = "That isn't a valid subnet!";
			}

			if (secondMask < 128 && secondMask != 0) {
				secondMask = 0;
				Message = "That isn't a valid subnet!";
			} else if (firstMask < secondMask) {
				secondMask = 0;
				Message = "That isn't a valid subnet!";
			}

			if (thirdMask < 128 && thirdMask != 0) {
				thirdMask = 0;
				Message = "That isn't a valid subnet!";
			} else if (secondMask < thirdMask) {
				thirdMask = 0;
				Message = "That isn't a valid subnet!";
			}

			if (fourthMask < 128 && fourthMask != 0) {
				fourthMask = 0;
				Message = "That isn't a valid subnet!";
			} else if (thirdMask < fourthMask) {
				fourthMask = 0;
				Message = "That isn't a valid subnet!";
			}

		}
		
		public void calculateNetmaskFromNetwork(int amount,String ipClass) {
			int bit = 0;
			int sum = 0;
			int exponent = 7;
			bit = findNumberOfBit(amount);
			if(ipClass.equals('C')){
				if(amount > 128){
					Message = "Out of range in Class C";
				}
				sum = sumOfExponentToZero(bit ,exponent);
				fourthMask = sum;
			}
			
			if(ipClass.equals('B')){
				if(amount > 16384){
					Message = "Out of range in Class B";
				}
				if(bit>8){
					bit = bit-8;
					sum = sumOfExponentToZero(bit ,exponent);
					thirdMask = 255;
					fourthMask = sum;
				} else{
					sum = sumOfExponentToZero(bit ,exponent);
					thirdMask = sum;
					fourthMask = 0;
				}
			}
			
			if(ipClass.equals('A')){
				if(amount > 4194304){
					Message = "Out of range in Class A";
				}
				if(bit>16){
					bit = bit-16;
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask =225;
					thirdMask = 225;
					fourthMask = sum;
				} else if(bit>8){
					bit = bit-8;
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask =225;
					thirdMask = sum;
					fourthMask = 0;
				} else{
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask = sum;
					thirdMask = 0;
					fourthMask = 0;
				}
			}
			
		}
		
		public void calculateNetmaskFromHost(int amount,String ipClass) {
			int bit = 0;
			int sum = 0;
			int exponent = 7;
			bit = findNumberOfBit(amount);
			if(checkFitCase(amount)) bit+=1;
			bit = 8 - bit;
			if(ipClass.equals('C')){
				if(amount > 254){
					Message = "Out of range in Class C";
				}
				sum = sumOfExponentToZero(bit ,exponent);
				fourthMask = sum;
			}
			
			if(ipClass.equals('B')){
				if(amount > 65534){
					Message = "Out of range in Class B";
				}
				if(bit>8){
					bit = bit-8;
					sum = sumOfExponentToZero(bit ,exponent);
					thirdMask = 255;
					fourthMask = sum;
				} else{
					sum = sumOfExponentToZero(bit ,exponent);
					thirdMask = sum;
					fourthMask = 0;
				}
			}
			
			if(ipClass.equals('A')){
				if(amount > 16777214){
					Message = "Out of range in Class A";
				}
				if(bit>16){
					bit = bit-16;
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask =225;
					thirdMask = 225;
					fourthMask = sum;
				} else if(bit>8){
					bit = bit-8;
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask =225;
					thirdMask = sum;
					fourthMask = 0;
				} else{
					sum = sumOfExponentToZero(bit ,exponent);
					secondMask = sum;
					thirdMask = 0;
					fourthMask = 0;
				}
			}
			
		}
		
		
		 public int sumOfExponentToZero(int time,int exponent){
			 int sum = 0;
			 for(int i = time; i > 0; i--){
					sum = sum + (int) Math.pow(2, exponent);
					exponent--;
				}
			 return sum;
		 }
		
		public static int findNumberOfBit(int amount) {
			int bit = 0;
			while(true){
				if(amount==1) return bit;
				if(amount%2 != 0) amount+=1;
				amount = amount/2;
				bit++;	
			}
		}
		
		public boolean checkFitCase(int amount){
			int starter = 0;
			int end = 0;
			if(amount >= Math.pow(2, 12)){
				starter = 12;
				end = 24;
			} else{
				starter = 0;
				end = 11;
			}
			for(int i=starter ; i <= end;i++) {
				if(amount == Math.pow(2, i)) return true;
			}
			return false;
		}

		public String getFirstadd() {
			return firstHost1 + "." + firstHost2 + "." + firstHost3 + "." + firstHost4;
		}

		public String getLastadd() {
			return lastHost1 + "." + lastHost2 + "." + lastHost3 + "." + lastHost4;
		}

		public int getNumberOfHosts() {
			int hosts;
			hosts = (wildMask1 > 0) ? (wildMask1 + 1) : 1;
			hosts *= (wildMask2 > 0) ? (wildMask2 + 1) : 1;
			hosts *= (wildMask3 > 0) ? (wildMask3 + 1) : 1;
			hosts *= (wildMask4 > 0) ? (wildMask4 + 1) : 1;
			hosts -= 2;
			return hosts;
		}

		public String getBroadcast() {
			return broadcast1 + "." + broadcast2 + "." + broadcast3 + "." + broadcast4;
		}

		public String getWildcard() {
			return wildMask1 + "." + wildMask2 + "." + wildMask3 + "." + wildMask4;
		}

		public String getSubnet() {
			return firstMask + "." + secondMask + "." + thirdMask + "." + fourthMask;
		}

		public String getNetwork() {
			return firstNet + "." + secondNet + "." + thirdNet + "." + fourthNet;
		}

		public void clearMessage() {
			Message = "";
		}

		public String getMessage() {
			return Message;
		}
		
		public static void main(String[] arg){
			System.out.println(findNumberOfBit(254));
		}

}
