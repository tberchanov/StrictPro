package com.strictpro.ui

import androidx.fragment.app.strictmode.FragmentStrictMode

object FragmentStrictMode {
    // TODO test this strict mode. Add it to lib?
    fun enable() {
        FragmentStrictMode.defaultPolicy = FragmentStrictMode.Policy.Builder()
            .detectFragmentReuse()
            .detectFragmentTagUsage()
            .detectSetUserVisibleHint()
            .detectWrongNestedHierarchy()
            .detectRetainInstanceUsage()
            .detectWrongFragmentContainer()
            .penaltyLog()
            .build()
    }
}