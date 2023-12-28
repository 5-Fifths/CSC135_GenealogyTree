// Name: Brian Truong
// Desc: Constructs trees for related persons

package genealogy;

import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.HashSet;

public class GenealogyTree {
	private static TreeNode root; 
	
	public static void main(String[] args) throws FileNotFoundException, NoSuchElementException {
		Scanner input = new Scanner(System.in);
		
		// Take in file data
		System.out.print("File name: ");
		String fileName = input.nextLine();
		Scanner file = getFile(fileName);
		
		// Initialize root
		constructTree(file);
		
		// Store names
		System.out.print("Ancestor Name: ");
		String ancestorName = input.nextLine();
		
		System.out.print("Descendant Name: ");
		String descendantName = input.nextLine();
		
		// Turn names into nodes to search for path
		TreeNode ancestor = findNode(ancestorName);
		TreeNode descendant = findNode(descendantName);
		
		// Confirm that user has input valid names
		if (ancestor == null || descendant == null) {
			System.out.println(ancestorName + " and/or " + descendantName +
					" is not part of the genealogy tree.");
			input.close();
			return;
		}
		
		// Store path between ancestor and descendant for output
		TreeNode[] path = findPath(ancestor, descendant);
		
		if (path[0] == null) {
			System.out.println(ancestorName + " doesn't have a descendant named " + descendantName + ".");
			
			input.close();
			return;
		}
		
		for (TreeNode n : path) {
			// Exit if there are no more nodes
			if (n == null) {
				break;
			}

			System.out.print(n.name + " -> ");
		}
		
		System.out.println("End");
		
		input.close();
	}
	
	// Returns path between an ancestor and a target descendant
	private static TreeNode[] findPath (TreeNode ancestor, TreeNode descendant) {
		// Maximum path size = tree depth
		TreeNode[] path = new TreeNode[maxDepth(root)]; 
		int memberCount = 0;
		
		// Guard clause for no children
		if (ancestor.childrenCount <= 0) return path;
		
		// Ensure that the ancestor has this descendant 
		if (findNode(ancestor, descendant) != null)
			path[memberCount++] = ancestor;
		else
			return path;
		
		// Path from ancestor to descendant
		while (ancestor.name != descendant.name) {
			for (int i = 0; i < ancestor.childrenCount; i++) {
				TreeNode child = ancestor.children[i];
				
				if (findNode(child, descendant) != null) { 
					ancestor = child;
					path[memberCount++] = ancestor;
				}
			}
		}
		
		return path;
	}
	
	// Constructs entire tree from file data
	private static void constructTree(Scanner input) {
		HashSet<String> set = new HashSet<>(); // names in tree
		
		// Number of entries
		int n = Integer.parseInt(input.nextLine()); 
		
		String line = input.nextLine();
		// Create root
		root = constructNode(line, set);
			
		// Create subsequent descendants
		for (int i = 0; i < n - 2; i++) {
			line = input.nextLine();
			constructNode(line, set);
		}
	}
	
	// Create a single node + connect children
	private static TreeNode constructNode(String line, HashSet<String> set) {
		StringTokenizer st = new StringTokenizer(line);
		String name = st.nextToken();
		TreeNode parent;
		
		// Number of descendants
		int n = Integer.parseInt(st.nextToken());
		
		// Make a new node or link existing node
		if (set.contains(name))
			parent = findNode(name);
		else {
			// Occurs only for root node unless .dat file is formatted differently
			parent = new TreeNode(name);
			set.add(name);
		}
		
		// Connect children to parent
		for (int i = 0 ; i < n; i++) {
			String childName = st.nextToken();
			TreeNode child = new TreeNode(childName);
			parent.addChild(child);
			set.add(child.name);
		}
		
		return parent;
	}
	
	// Return target node, if it exists under currNode
	private static TreeNode findNode(TreeNode currNode, String target) {
		// Reached a leaf node without finding the target
		if (currNode == null) return null;
		
		// Target has been found
		String name = currNode.name.toLowerCase();
		if (name.equals(target.toLowerCase())) 
			return currNode;
		
		// Iterate through children to find if target is their descendant
		TreeNode res = null;
		for (int i = 0; res == null && i < currNode.childrenCount; i++)
			res = findNode(currNode.children[i], target);
		
		return res;
	}
	
	private static TreeNode findNode(String target) {
		return findNode(root, target);
	}
	
	private static TreeNode findNode(TreeNode currNode, TreeNode target) {
		return findNode(currNode, target.name);
	}
	
	// Return tree depth
	private static int maxDepth(TreeNode node) {
		if (node == null) return 0;
		
		int[] depths = new int[node.children.length];
		
		for (int i = 0; i < depths.length; i++)
			depths[i] = maxDepth(node.children[i]) + 1;
		
		return maxArray(depths);
	}
	
	// Return max of an array
	private static int maxArray(int[] arr) {
		int max = Integer.MIN_VALUE;
		
		for (int i : arr)
			max = Math.max(i, max);
		
		return max;
	}
	
	// Grab file if it exists
	private static Scanner getFile(String fileName) {
		Scanner input = null;
		
		try {
			File file = new File(fileName);
			input = new Scanner(file);
		} 
		catch (FileNotFoundException e) {
			System.out.printf("'%s' not found.\n", fileName);
		}
		
		return input;
	}
}
