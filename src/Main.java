import java.util.*;
import java.util.stream.Collectors;

public class Main {


    public void FIFO(Page[] pages, int capacity){

        Queue<Integer> queue = new LinkedList<>();
        List<Integer> list = new LinkedList<>();
        int faults = 0;

        for(Page page: pages){
            //test probability
            Random random = new Random();
            double randomProb = random.nextDouble();
            int pageNum = page.getNum();

            if(page.getProb() > randomProb){
                //frame is not full
                if(list.size() < capacity){
                    //test if the page number already exist
                    if(!list.contains(pageNum)){
                        list.add(pageNum);
                        faults ++;
                        queue.add(pageNum);
                        System.out.println(queue.toString());
                    }
                }
                //frame is full
                else{
                    //test if the page number already exist
                    if(!list.contains(pageNum)){
                        int index = list.indexOf(queue.poll());
                        list.set(index,pageNum);
//                        list.remove(queue.poll());
//                        list.add(pageNum);
                        queue.add(pageNum);
                        faults ++;
                        System.out.println(list.toString());
                    }
                }
            }
        }
        System.out.println("Number of faults is: " + faults);
    }

    //the helper method for CLOCK: return the index of page in the list
    //return -1 if not contained
    public int indexOfContainedPage(List<Page> list, int pageNum){
        for(int i = 0; i < list.size(); i++ ){
            if(list.get(i).getNum() == pageNum)
                return i;
        }
        return -1;
    }

    public void print(List<Page> pages){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(Page page1: pages){
            builder.append(page1.getNum());
            builder.append(',');
        }
        builder.deleteCharAt(builder.length() -1);
        builder.append("]");
        System.out.println(builder.toString());
    }


    public void CLOCK(Page[] pages, int capacity){

        List<Page> storage = new LinkedList<>();
        List<Page> process = new LinkedList<>();
        int counter = 0;
        int faults = 0;
        for(Page page: pages){
                //test probability
                Random random = new Random();
                double randomProb = random.nextDouble();
                int pageNum = page.getNum();

                if(page.getProb() > randomProb){
                    //if the storage is not full
                    int pageIndex = indexOfContainedPage(storage,pageNum);
                    if(storage.size() < capacity){
                        //test if the page number is contained
                        //return -1 if not exist
                        if(pageIndex == -1){
                            storage.add(page);
                            faults ++;
                            process.add(page);
                            print(process);
                        }
                        else{
                            storage.get(pageIndex).grantSecondChance();
                        }
                    }
                    //the storage is full
                    else{
                        //paegNum is not contained
                        if(pageIndex == -1){
                            //iterate if has second chance
                            while(storage.get(0).getSecondChance()){ //if true, add to the end of list
                                Page temp = storage.remove(0);
                                temp.cancelSecondChance();
                                storage.add(temp);
                                counter = (counter+1) % capacity ;
                            }
                            //page without second chance
                            storage.remove(0);
                            //add new number to the end, prob does not matter here
                            storage.add(new Page(pageNum,1));
                            faults ++;
                            process.set(counter,new Page(pageNum,1));
                            print(process);
                            counter = (counter+1) % capacity ;
                        }
                        else{
                            storage.get(pageIndex).grantSecondChance();
                        }
                    }
                }
        }
        System.out.println("Number of faults is: " + faults);
    }


    public void tests(String test, Page[] pages) {
        //case1: equi-probable test: 60% for each
        //case2: strongly biased probability for lower-numbered pages to be requested
        //the exponential distribution of probability is 0.7e^(-0.7*num)
        //case 3： very strongly biased probability to request any of 3<k<10 pages
        //probability is 90% for 3<k<10; other wise 0.7e^(-0.7*num)
        switch (test) {
            case ("test1") -> {
                for (Page page : pages) {
                    page.setProb(0.6);
                }
                System.out.println("Test1:");
                System.out.println("FIFO test for frame size 3:");
                FIFO(pages, 3);
                System.out.println("FIFO test for frame size 5:");
                FIFO(pages, 5);
                System.out.println("CLOCK test for frame size 3:");
                CLOCK(pages, 3);
                System.out.println("CLOCK test for frame size 5:");
                CLOCK(pages, 5);
                System.out.println("--------------------------");
            }
            case ("test2") -> {
                for (Page page : pages) {
                    page.setProb(0.7 * Math.exp(-0.7 * page.getNum()));
                }
                System.out.println("Test2:");
                System.out.println("FIFO test for frame size 3:");
                FIFO(pages, 3);
                System.out.println("FIFO test for frame size 5:");
                FIFO(pages, 5);
                System.out.println("CLOCK test for frame size 3:");
                CLOCK(pages, 3);
                System.out.println("CLOCK test for frame size 5:");
                CLOCK(pages, 5);
                System.out.println("--------------------------");
            }
            case ("test3") -> {
                for (Page page : pages) {
                    if (page.getNum() < 10 && page.getNum() > 3) {
                        page.setProb(0.9);
                    } else
                        page.setProb(0.7 * Math.exp(-0.7 * page.getNum()));
                }
                System.out.println("Test3:");
                System.out.println("FIFO test for frame size 3:");
                FIFO(pages, 3);
                System.out.println("FIFO test for frame size 5:");
                FIFO(pages, 5);
                System.out.println("CLOCK test for frame size 3:");
                CLOCK(pages, 3);
                System.out.println("CLOCK test for frame size 5:");
                CLOCK(pages, 5);
            }
            default -> throw new IllegalArgumentException("no such test");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        //initialize the pages with size 15 and probability 1
        //number in each page is ranging from 0 to 9
        Page p1 = new Page(0, 1);
        Page p2 = new Page(2, 1);
        Page p3 = new Page(1, 1);
        Page p4 = new Page(7, 1);
        Page p5 = new Page(4, 1);
        Page p6 = new Page(0, 1);
        Page p7 = new Page(1, 1);
        Page p8 = new Page(0, 1);
        Page p9 = new Page(3, 1);
        Page p10 = new Page(2, 1);
        Page p11 = new Page(3, 1);
        Page p12 = new Page(5, 1);
        Page p13 = new Page(8, 1);
        Page p14 = new Page(6, 1);
        Page p15 = new Page(9, 1);
        Page[] testPages = new Page[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15};
        main.tests("test1", testPages);
        main.tests("test2", testPages);
        main.tests("test3", testPages);

    }
}
