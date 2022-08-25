package com.backend.connectable.exception.sequence;

import com.backend.connectable.exception.sequence.ValidationGroups.NotEmptyGroup;
import com.backend.connectable.exception.sequence.ValidationGroups.PatternCheckGroup;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {
}
