public class MultiwayTreeNode{
    /*We will use this class when we store MST in a multiway tree */
    
    public String id;
    MultiwayTreeNode parent;
    MultiwayTreeNode firstChild;
    MultiwayTreeNode nextSibling;
    MultiwayTreeNode prevSibling;

    public MultiwayTreeNode(String id) {
        this.id = id;
        this.parent = null;
        this.firstChild = null;
        this.nextSibling = null;
        this.prevSibling = null;
    }

    public void addChild(MultiwayTreeNode ch){
        ch.parent=this;

        //case 1: new child is the first child of parent node
        if(this.firstChild == null){
            this.firstChild = ch;
            ch.prevSibling = null;
            ch.nextSibling = null;
        }

        //case 2: new child is not the first child of parent node
        else{
            MultiwayTreeNode sibling = this.firstChild;
            while(sibling.nextSibling != null){
                sibling = sibling.nextSibling;
            }
            sibling.nextSibling = ch;
            ch.prevSibling = sibling;
        }
    }

}