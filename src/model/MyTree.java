package model;

import java.util.*;

/**
 * A family tree of an individual
 */
public class MyTree {
    private Person rootPerson;
    private List<Person> family;

    /**
     * Constructs a Family tree with the rootPerson initialized
     * @param root the root person of the family tree
     */
    public MyTree(Person root) {
        rootPerson = root;
        family = new ArrayList<Person>();
//        family.add(rootPerson);

    }
    public void addFamilyMember(Person newMember) {
        family.add(newMember);
    }

    public Person[] getTreeArray() {
        Person[] familyTree = new Person[family.size()];
        familyTree = family.toArray(familyTree);
        return familyTree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyTree)) return false;
        MyTree myTree = (MyTree) o;
        return rootPerson.equals(myTree.rootPerson) &&
                family.equals(myTree.family);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootPerson, family);
    }
}
