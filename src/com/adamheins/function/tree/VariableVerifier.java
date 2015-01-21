package com.adamheins.function.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariableVerifier {
    
    Map<String, Function> varMap;
    Set<String> varSet;

    
    /**
     * Constructor.
     * 
     * @param varMap Map of variables to functions.
     */
    VariableVerifier(Map<String, Function> varMap) {
        this.varMap = varMap;
        this.varSet = new HashSet<String>();
    }
    
    
    /**
     * Verifies that the new variable and function do not cause an infinitely recursive loop with
     * another variable.
     * 
     * @param var The new variable.
     * @param func The value of the new variable.
     * 
     * @return True if the variable is acceptable, false otherwise.
     */
    boolean verify(String var, Function func) {
        
        // This variable is same as an ancestor
        if (varSet.contains(var))
            return false;
        
        // No function, branch is done.
        if (func == null)
            return true;
        
        // Add to the set. This variable is now an ancestor.
        varSet.add(var);
        
        List<String> vars = func.getVariables();
        for (String v : vars) {
            if (!verify(v, varMap.get(v)))
                return false;
        }
        
        // Remove it from the set. Going back up the branch.
        varSet.remove(var);
        return true;
    }
}
