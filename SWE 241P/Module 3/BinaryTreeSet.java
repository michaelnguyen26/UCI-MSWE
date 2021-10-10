class TreeNode{
    String val;
    TreeNode leftChild;
    TreeNode rightChild;

    public TreeNode(String word){
        val = word;
        leftChild = null;
        rightChild = null;
    }
}

public class BinaryTreeSet {
    TreeNode root;
    private int size = 0;
    TreeNode nextNode = root;


    public BinaryTreeSet(){
        root = null;
    }

    public boolean contains(String word){
        return traverse(root, word);
    }

    public boolean traverse(TreeNode root, String word){
        if(root == null){ //basis case
            return false;
        }

        if(root.val.equals(word)){
            return true;
        }


        if(traverse(root.leftChild, word) || traverse(root.rightChild, word)){
            return true;
        } 

        return false;

    }

    public boolean add(String word){
        if(contains(word)){
            return false;
        }

        root = recursive(root, word);
        size++;
        return true;  
    }

    private TreeNode recursive(TreeNode root, String word){
        if(root == null){
            root = new TreeNode(word);
            return root;
        }

        else if(root.leftChild == null && word.length() <= root.val.length()){
            root.leftChild = new TreeNode(word);
        }

        else if(root.rightChild == null && word.length() > root.val.length()){
            root.rightChild = new TreeNode(word);
        }

        else{
            if(word.length() <= root.val.length()){
                root.leftChild = recursive(root.leftChild, word);
            }else{
                root.rightChild = recursive(root.rightChild, word);
            }
            //when this executes, this will have a diffrent second "frame" during the recursion call. 
            //This means that now my root will be root.leftChild. When this second "frame" ends and returns
            //root, then it will return itself back to the "original frame" and assign to root.leftchild of the
            //original root from the beginning.      
        }
        return root;
    }

    public void printInPreOrder(TreeNode root){
        if(root == null){
            return;
        }

        System.out.print(root.val + " ");

        printInPreOrder(root.leftChild);
        printInPreOrder(root.rightChild);
    }

    public int size(){
        if(root == null){
            return 0;
        }
        return size;
    }
}