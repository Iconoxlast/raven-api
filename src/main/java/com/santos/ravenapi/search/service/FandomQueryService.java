package com.santos.ravenapi.search.service;

import java.util.List;

import com.santos.ravenapi.model.vo.IssueDetailsVO;
import com.santos.ravenapi.search.enums.PublisherEndpointEnum;

public interface FandomQueryService {

	List<IssueDetailsVO> getAppearances(PublisherEndpointEnum publisher, String character);

}
