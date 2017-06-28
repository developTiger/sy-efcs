package com.wxzd.efcs.business.repositorys;

import com.wxzd.efcs.business.domain.entities.PalletDispatch;
import com.wxzd.efcs.business.domain.entities.PalletMoveDetail;
import com.wxzd.efcs.business.domain.entities.form.FmPalletize;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.gaia.jdbc.common.core.SqlUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.web.i18n.I18nContext;
import com.wxzd.wms.core.domain.entities.StorageLocation;
import com.wxzd.wms.ddd.DomainRepository;
import com.wxzd.wms.ddd.GaiaQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouzh on 2017/4/18.
 */
@Repository
public class PalletMoveDetailRepository extends DomainRepository<PalletMoveDetail> {

}
