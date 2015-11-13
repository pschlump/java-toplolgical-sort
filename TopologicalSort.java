import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
 
public class TopologicalSort {

    public static void main(String[] args) throws Exception {

		// TODO: Check that args has a file and is not empty.  If empty then error out.

		// List<String> lines = Files.readAllLines(Paths.get("input.txt"), StandardCharsets.UTF_8);
		List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);

		// Parse input of "lines" -> graph(grp) and inDegree(ideg)
		TsProc pr = new TsProc( lines );
		pr.dumpGraphAndInDegree();

		// Sort 
		pr.topologicalSort();

		// output
		pr.outputSortedResults();

    }

}

