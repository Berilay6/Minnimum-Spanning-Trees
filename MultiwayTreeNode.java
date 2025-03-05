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

        if(this.firstChild == null){
            this.firstChild = ch;
        }

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