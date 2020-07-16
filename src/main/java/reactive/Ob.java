package reactive;

import java.util.*;

public class Ob {

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i);     // push
            }
        }
    }


    public static void main(String[] args) {
        Observer observer = (o, arg) -> System.out.println(arg);
        /* 윗줄은 아래 코드와 같음
        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        };
         */

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(observer);  // observer가 던지는 이벤트를 intObservable가 받음!
        intObservable.run();
    }
}
