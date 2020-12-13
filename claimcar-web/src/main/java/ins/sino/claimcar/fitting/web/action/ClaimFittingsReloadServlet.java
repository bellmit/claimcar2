/******************************************************************************
* CREATETIME : 2015年12月19日 下午4:44:38
* FILE       : ins.sino.claimcar.fitting.web.action.ClaimFittingsReloadServlet
******************************************************************************/
package ins.sino.claimcar.fitting.web.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <pre></pre>
 * @author ★yangkun
 */
public class ClaimFittingsReloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

	}

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		try{
			String lossNo = request.getParameter("lossNo");
			String tableIndex = request.getParameter("tableIndex");
			String operateType = request.getParameter("operateType");
			
			if(!"".equals(tableIndex) && !"null".equals(tableIndex) && tableIndex!=null){
				request.setAttribute("tableIndex",tableIndex);
			}else{
				request.setAttribute("tableIndex","0");
			}
			System.out.println(lossNo);//defLossFittings/queryDefLoss.do?lossNo="+lossNo+"&operateType="+operateType
			RequestDispatcher disp = request.getRequestDispatcher("/defLossFittings/queryDefLoss.do");
			request.setAttribute("lossNo",lossNo);
			request.setAttribute("operateType",operateType);
			
			disp.forward(request,response);
			
//			response.sendRedirect("/claimcar/defLossFittings/queryDefLoss.do");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		doGet(request,response);
	}
}
