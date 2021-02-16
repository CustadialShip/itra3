package com.custadialship;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            List<String> arguments = Arrays.asList(args);
            if(arguments.size() <= 1){
                throw new Exception("Wrong number of elements (<1)");
            } else if (arguments.size() % 2 == 0) {
                    throw new Exception("Wrong number of elements (even)");
            } else if(arguments.stream().anyMatch(i -> Collections.frequency(arguments, i) >1)){
                throw new Exception("Wrong number of elements (identical elements)");
            }
            Game game = new Game(args);
            while (game.getIsRun()) {
                game.update();
                game.printMenu();
                int moveOfUser = sc.nextInt();
                while (moveOfUser < 0 || moveOfUser > args.length) {
                    System.out.println("Invalid number. Try again: ");
                    moveOfUser = sc.nextInt();
                }
                if (moveOfUser == 0) {
                    game.stop();
                } else {
                    System.out.println("Computer move:" + (game.getMoveOfComputerName()));
                    game.setMoveOfUser(moveOfUser);
                    System.out.println(game.resultOfGame());
                    System.out.println("Key: " + Game.display(game.getKey()));
                    System.out.println("------------------------------------------------------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
