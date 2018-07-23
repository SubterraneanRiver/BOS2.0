package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>,
JpaSpecificationExecutor<Courier> {

	
	//这里需要使用自定义命名规则创建一个方法
	@Query(value="update Courier set deltag='1' where id=?")
	@Modifying   //加了这个才能实现修改
	void updateDelTag(Integer id);

	@Query(value="update Courier set deltag='' where id=?")
	@Modifying   //加了这个才能实现修改
	void addDelTag(Integer id);

	
	
}
