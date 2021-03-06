package com.hubber.TreeSync;

import java.io.File;

public class TItemFilename extends TItemGeneric {

    public TItemFilename(File file, File root) {
        super(file, root);
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        if(!(other instanceof TItemFilename)) {
            return false;
        }

        return this.relative.equals(((TItemFilename)other).relative);
    }

    @Override
    public int hashCode() {
        return relative.hashCode();
    }
}
