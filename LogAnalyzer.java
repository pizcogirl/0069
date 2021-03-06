/**
 * Read web server data and analyse
 * hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version 2011.07.31
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Where to calculate the successfully hourly access counts.
    private int[] hourSuccessCounts;
    // Where to calculate the daily access counts.
    private int[] dailyCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the array object to hold the hourly
        // access counts wich was successfull.
        hourSuccessCounts = new int[24];
        // Create the array object to hold the daily
        // access counts.
        dailyCounts = new int[31];
        // Create the reader to obtain the data from the filename
        reader = new LogfileReader(filename);
    }

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the array object to hold the hourly
        // access counts wich was successfull.
        hourSuccessCounts = new int[24];
        // Create the array object to hold the daily
        // access counts.
        dailyCounts = new int[31];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /** 
     * Analyze the hourly accesses in the given date
     *
     * @param day     The given day
     * @param month The given month
     * @param year  The given year
     */
    public void analyzeDataFromGivenDay(int day, int month, int year)
    {
        // Leemos el log, si el a�o, dia y mes coincide con los introducidos, contamos,
        // sino lo descartamos
        while(reader.hasNext()) 
        {
            LogEntry entry = reader.next();
            if ((entry.getYear() == year) && (entry.getMonth() == month) && (entry.getDay() == day))
            {
                int hour = entry.getHour();
                hourCounts[hour]++;
            }
        }
    }

    /**
     * Analyze the daily access data from the log file.
     */
    public void analyzeDailyData()
    {
        while(reader.hasNext()) 
        {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dailyCounts[day - 1]++;
        }
    }

    /**
     * Analyze the successfully hourly access data from the log file.
     */
    public void analyzeSuccessHourlyData()
    {
        // Iteramos sobre el log
        while(reader.hasNext()) 
        {
            LogEntry entry = reader.next();
            // Si en el campo success tenemos 200, sumamos uno, sino no
            if (entry.getAccess() == 200)
            {
                int hour = entry.getHour();
                hourSuccessCounts[hour]++;
            }
        }
    }

    /**
     * Devuelve el numero de accesos al servidor web registrados en ese log.
     * Si aun no se ha analizado el log, devuelve 0.
     */
    public int numberOfAccesses()
    {
        // Creamos un indice un entero para devolver al final del metodo
        int access = 0;
        int index = 0;
        // Recorremos la array y vamos sumando en access los accesos de cada hora
        while(index < hourCounts.length)
        {
            access = access + hourCounts[index];
            index++;
        }
        return access;
    }

    /**
     * Devuelve a que hora tubo m�s accesos el servidor.
     * Si no se ha analizado el log, devuelve -1.
     */
    public int busiestHour()
    {
        // Creamos un indice y un entero para devolver al final del metodo
        int busiestHour = -1;
        int index = 0;
        // Recorremos la array y vamos guardando en busiestHour la hora si
        // el numero de accesos fue mayor que en la hora anterior
        while(index < hourCounts.length)
        {
            if (hourCounts[index] > hourCounts[busiestHour + 1])
            {
                busiestHour = (index);
            }
            index++;
        }
        return busiestHour;
    }

    /**
     * Devuelve a que hora tubo menos accesos el servidor.
     * Si no se ha analizado el log, devuelve -1.
     */
    public int quietestHour()
    {
        // Creamos un indice y un entero para devolver al final del metodo
        int quietestHour = -1;
        int index = 0;
        // Recorremos la array y vamos guardando en quietestHour la hora si
        // el numero de accesos fue menor que en la hora anterior
        while(index < hourCounts.length)
        {
            if (hourCounts[index] < hourCounts[quietestHour + 1])
            {
                quietestHour = (index);
            }
            index++;
        }
        return quietestHour;
    }

    /**
     * Devuelve la primera de las dos horas consecutivas tuvo m�s accesos el servidor.
     * Si no se ha analizado el log, devuelve -1.
     */
    public int busiestTwoHour()
    {
        // Creamos un indice y un entero para devolver al final del metodo
        int busiestTwoHour = -1;
        int index = 0;
        // Creamos un int para guardar el numero de accesos por periodos de dos horas
        int accessTwoHours = 0;
        // Recorremos la array y vamos guardando en busiestTwoHour la hora si
        // el numero de accesos fue mayor durante esa hora y la siguiente
        while(index < (hourCounts.length - 1))
        {
            if ((hourCounts[index] + hourCounts[index+1]) > accessTwoHours)
            {
                accessTwoHours = hourCounts[index] + hourCounts[index+1];
                busiestTwoHour = (index);
            }
            index = index + 1;
        }
        return busiestTwoHour;
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }

    /**
     * Print the daily counts.
     * These should have been set with a prior
     * call to analyzeDailyData.
     */
    public void printDailyCounts()
    {
        System.out.println("Day: Count");
        for(int day = 0; day < dailyCounts.length; day++) {
            System.out.println((day +1) + ": " + dailyCounts[day]);
        }
    }
    
        /**
     * Print the successfull hourly counts.
     * These should have been set with a prior
     * call to analyzeSuccessHourlyData.
     */
    public void printSuccessHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourSuccessCounts.length; hour++) {
            System.out.println(hour + ": " + hourSuccessCounts[hour]);
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts2()
    {
        System.out.println("Hr: Count");
        int hour = 0;
        while (hour < hourCounts.length) 
        {
            System.out.println(hour + ": " + hourCounts[hour]);
            hour++;
        }
    }

    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
}
