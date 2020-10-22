public class Page {

   private double prob;
   private final int number;
   private  boolean secondChance = false;

   public Page(int number, double prob){
      this.number = number;
      this.prob = prob;
   }

   public void setProb(double prob){
    this.prob = prob;
  }

   public double getProb(){
     return this.prob;
   }

   public int getNum(){
    return this.number;
  }

   public void grantSecondChance(){
      secondChance = true;
   }

   public void cancelSecondChance(){
      secondChance = false;
   }

   public boolean getSecondChance(){
      return secondChance;
   }

}
