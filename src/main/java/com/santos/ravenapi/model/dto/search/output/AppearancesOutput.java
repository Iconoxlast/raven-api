package com.santos.ravenapi.model.dto.search.output;

import java.util.ArrayList;

public record AppearancesOutput(ArrayList<IssueOutput> appearances) implements OutputDTO {

}
