package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	
	public List<Customer> findByFixedAreaIdIsNull();//这是springjapd的特殊规则

	public List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query("update Customer set fixedAreaId=? where id=?")
	@Modifying
	public void updateFixedAreaId(String fixedAreaId, Integer id);//根据规则进行创建的方法

	
	@Query("update Customer set fixedAreaId = null where fixedAreaId=?")
	@Modifying   //要想修改成功必须执行该操作，否则只能进行查询
	public void clearFixedAreaId(String fixedAreaId);

	public Customer findByTelephoneAndPassword(String telephone, String password);

	public Customer findByTelephone(String telephone);
@Query("update Customer set type=1 where telephone=?")
@Modifying
	public void updateType(String telephone);
	
	



	
	
	
	
}
