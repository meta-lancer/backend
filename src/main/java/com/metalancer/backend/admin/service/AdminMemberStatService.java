package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.MemberRegisterDailyStat;
import com.metalancer.backend.admin.domain.MemberRegisterStat;
import com.metalancer.backend.common.constants.PeriodType;
import java.util.List;

public interface AdminMemberStatService {

    MemberRegisterStat getAdminMemberStat();

    List<MemberRegisterDailyStat> getAdminMemberRegisterDailyStat(String beginDate, String endDate,
        PeriodType periodType);
}