// Name: Brian Truong
// Desc: Node to store data about person and their children

package genealogy;

public class TreeNode {
	public int childrenCount;
	public TreeNode[] children;
	public String name;
	
	public TreeNode(String name) {
		this.name = name;
		children = new TreeNode[2];
	}
	
	// add child
	public void addChild(TreeNode child) {
		// ensure no overflow
		if (childrenCount + 1> children.length) 
			resize();
		
		children[childrenCount++] = child;
	}
	
	public void addChild(String s) {
		addChild(new TreeNode(s));
	}
	
	// add array of children
	public void addChild(TreeNode[] children) {
		for (TreeNode c : children) addChild(c);
	}
	
	// resize children array if insufficient array space
	private void resize() {
		TreeNode[] temp = new TreeNode[children.length * 2];
		
		for (int i = 0; i < childrenCount; i++)
			temp[i] = children[i];
		
		children = temp;
	}
}
