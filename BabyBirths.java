/**
 * Print out the names for which 100 or fewer babies were born in a chosen CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

public class BabyBirths {
    public void printNames () {
    FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }
    
    // загальні дані
    public void totalBirths(int year) {
        FileResource fr = pathFile(year);
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int countBoyName = 0;
        int countGirlName = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if(rec.get(1).equals("M")){
                totalBoys += numBorn;
                countBoyName++;
            }else{
                totalGirls +=numBorn;
                countGirlName++;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("total boys = " + totalBoys);
        System.out.println("total girls = " + totalGirls);
        System.out.println("count name boys = " + countBoyName);
        System.out.println("count name girl = " + countGirlName);
        System.out.println("count total name = " + (countBoyName + countGirlName));
    }
    
    public FileResource pathFile(int year){
        return new FileResource("us_babynames/us_babynames_by_year/yob" + year +".csv");
    }
    
    // My Methods
    // rank
    public int getRank(int year, String name, String gender) {
        FileResource fr = pathFile(year);
        int rank = 1;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            // Increment rank if gender matches param
            if (rec.get(1).equals(gender)) {
                // Return rank if name matches param
                if (rec.get(0).equals(name)) {
                    return rank;
                }
                rank++;
            }
            
        }
    
        return -1;
    }
    
    // name
    public String getName(int year, int rank, String gender){
        int currentRank = 0;
        String name = "";
        FileResource fr = pathFile(year);
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            
            // Get its rank if gender matches param
            if (rec.get(1).equals(gender)) {
                // Return last name whose rank matches param (file is already in order of rank)
                if (currentRank == rank) {
                    return name;
                }
                name = rec.get(0);
                currentRank++;
            }
        } 
        
        return "NO NAME";
    }
    
    // яке могло бути імя по рейтинку в іншому році
    public void whatIsNameInYear(String name, int year, int newYear, String gender){
        
        int rank = getRank(year, name, gender); 
        System.out.println("The rank of Owen is " + rank);
        // Determine name born in newYear that is at the same rank and gender
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " 
                            + newName + " if born in " + newYear);
    }
    
    // найвищий рейтинг у файлах
        public int yearOfHighestRank(String name, String gender) {
        // Allow user to select a range of files
        DirectoryResource dir = new DirectoryResource();
        int year = 0;
        int rank = 0;
        // For every file the user selected
        for (File f : dir.selectedFiles()) {
            // Extract current year from file name
            int currentYear = Integer.parseInt(f.getName().substring(3,7));
            // Determine rank of name in current year
            int currentRank = getRank(currentYear, name, gender);
            System.out.println("Rank in year " + currentYear + ": " + currentRank);
            // If current rank isn't invalid
            if (currentRank != -1) {
                // If on first file or if current rank is higher than saved rank
                if (rank == 0 || currentRank < rank) {
                    // Update tracker variables
                    rank = currentRank;
                    year = currentYear;
                } 
            }
        }
        
        if (year == 0) {
            return -1; 
        }
        return year;
    }
    
    // середній рейтинг по файлам
    public double getAverageRank(String name, String gender) {
        // Allow user to select a range of files
        DirectoryResource dir = new DirectoryResource();
        double totalRank = 0.0;
        int count = 0;
        for (File f : dir.selectedFiles()) {
            // Extract current year from file name
            int currentYear = Integer.parseInt(f.getName().substring(3,7));
            // Determine rank of name in current year
            int currentRank = getRank(currentYear, name, gender);
            // Add rank to total and increment count
            totalRank += currentRank;
            count++;
        }
        // Return calculated average rank
        if (totalRank == 0) {
            return -1;
        }
        double average = totalRank/count;
        return average;
    }
    
    // скільки імен більше ніж це
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        // Get number of births for given name and gender
        int numOfBirths = 0;
        FileResource fr = pathFile(year);
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                numOfBirths = Integer.parseInt(rec.get(2));
            }
        }
        
        // Add up number of births greater than that for given name and gender
        int totalBirths = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            String currentGender = rec.get(1);
            // If name is not given name AND current gender matches param 
            // AND current num of births is higher than for given name, 
            if (!rec.get(0).equals(name) && currentGender.equals(gender) && 
                Integer.parseInt(rec.get(2)) >= numOfBirths) {
                // Add number of births to total
                totalBirths += Integer.parseInt(rec.get(2));
            }
        }
        return totalBirths;
    }
    
    // TEST //
    //public void testTotalBirths(){
        //FileResource fr = new FileResource("us_babynames/us_babynames_test/example-small.csv");
        //totalBirths(fr);
    //}
    
    public void testMyHomeWork(){
        totalBirths(1900);
        
        //int nameRank = getRank(2012, "Mason", "M");
        //System.out.println("rank your name " + nameRank);
        
        //String name = getName(2012, 2, "M");
        //System.out.println("your name " + name);
        
        //whatIsNameInYear("Isabella", 2012, 2014, "F");
        
        //int numbeYear = yearOfHighestRank("Mason", "M");
        //System.out.println("your year " + numbeYear);
        
        //double average = getAverageRank("Jacob", "M");
        //System.out.println("your average rank " + average);
        
        //int higher = getTotalBirthsRankedHigher(2012, "Ethan", "M");
        //System.out.println("your total higher " + higher);
    }
}
