package com.ippon.account.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.stereotype.Repository;

import com.ippon.account.domain.Account;
import com.ippon.account.domain.Account_;
import com.ippon.account.domain.Address;
import com.ippon.account.domain.Address_;

@Repository("addressDaoService")
public class AddressDaoServiceImpl extends BaseEntityDaoServiceImpl<Address> implements AddressDaoService {

  public AddressDaoServiceImpl() {
    super(Address.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S extends Object> List<Address> getBy(String columnName, String columnValue) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Address> cq = cb.createQuery(type);
    Root<Address> root = cq.from(type);
    cq.select(root);
    Predicate predicate = null;
    SingularAttribute<Address, S> singularAttribute = getSingularAttribute(columnName);
    if (singularAttribute.getJavaType().equals(String.class)) {
      predicate = cb.like(cb.upper((Expression<String>) root.get(singularAttribute)), "%" + columnValue.toUpperCase() + "%");
    } else if (singularAttribute.getJavaType().equals(Account.class)) {
      CriteriaBuilder builder = em.getCriteriaBuilder();
      predicate = builder.like(cb.upper(root.join(Address_.account).get(Account_.username)), columnValue.toUpperCase() + "%");
    } else {
      predicate = cb.equal(root.get(singularAttribute), columnValue);
    }
    cq.where(predicate);
    TypedQuery<Address> q = em.createQuery(cq);
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  public <S extends Object> SingularAttribute<Address, S> getSingularAttribute(String columnName) {
    try {
      return (SingularAttribute<Address, S>) Address_.class.getDeclaredField(columnName).get(Address_.class);
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

}
