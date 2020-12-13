package ins.sino.claimcar.base.service.spring;

import ins.sino.claimcar.other.service.HolidayManageService;
import ins.sino.claimcar.other.service.HolidayService;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("holidayService")
public class HolidayServiceSpringImpl implements HolidayService{
	@Autowired
	HolidayManageService holidayManageService;
	
	@Override
	public Boolean existHoliday(String userCode){
		return holidayManageService.existHoliday(userCode);
	}

}
