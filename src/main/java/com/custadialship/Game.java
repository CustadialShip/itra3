package com.custadialship;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
    private boolean isRun = true;
    private byte[] key;
    private final List<String> elementsOfGame;
    private final int numberOfItem;
    private String HmacOfComputerMove;
    private  int moveOfComputerNumber;
    private  String moveOfComputerName;
    private int moveOfUserNumber;
    private final int numberOfWinningItems;
    private final List<Integer> winningItems;
    SecureRandom random = new SecureRandom();


    Game(String[] elementsOfGame) throws NoSuchAlgorithmException {
        byte[] newKey = new byte[16];
        random.nextBytes(newKey);
        this.key = newKey;

        this.elementsOfGame = Arrays.asList(elementsOfGame);
        this.numberOfItem = elementsOfGame.length;
        numberOfWinningItems = (numberOfItem - 1) / 2;
        winningItems = calculationOfWinningItem();

        this.moveOfComputerNumber = (int) (Math.random() * numberOfItem);
        this.moveOfComputerName = this.elementsOfGame.get(moveOfComputerNumber);

        this.HmacOfComputerMove = getHash(moveOfComputerName + key);
    }

    public void stop(){
        isRun = false;
    }

    public void printMenu(){
        System.out.println("HMAC:");
        System.out.println(getHmacOfComputerMove());
        System.out.println("Available moves:");
        IntStream.range(0, numberOfItem)
                .forEach(index -> System.out.println((index + 1) + " - " + elementsOfGame.get(index)));
        System.out.println("0 - Exit");
        System.out.println("Enter your move:");
    }

    public void update() throws NoSuchAlgorithmException {
        this.key = random.generateSeed(16);
        this.moveOfComputerNumber = (int) (Math.random() * numberOfItem);
        this.moveOfComputerName = this.elementsOfGame.get(moveOfComputerNumber);
        this.HmacOfComputerMove = getHash(moveOfComputerName + Arrays.toString(key));
    }

    private List<Integer> calculationOfWinningItem(){
        List<Integer> winningItem = new ArrayList<>(numberOfWinningItems);
        IntStream.range(0, numberOfWinningItems).
                forEach(i -> winningItem.add(i, (i + moveOfComputerNumber + 1) % (numberOfItem + 1)));
        return winningItem;
    }

    static String getHash(String inputString) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashBytes = digest.digest(
                inputString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public String resultOfGame(){
        if (moveOfUserNumber == moveOfComputerNumber)
            return "Draw";
        if(winningItems.stream().anyMatch(x -> x == moveOfComputerNumber)) {
            return "You win!";
        }
        return "You lose";
    }

    String getMoveOfComputerName(){
        return moveOfComputerName;
    }

    boolean getIsRun(){
        return isRun;
    }

    public byte[] getKey() {
        return key;
    }

    String getHmacOfComputerMove(){
        return HmacOfComputerMove;
    }

    void setMoveOfUser(int i){
        this.moveOfUserNumber = i - 1;
    }
}
