/**
 * Copyright Notice
 *
 * This is a work of the U.S. Government and is not subject to copyright 
 * protection in the United States. Foreign copyrights may apply.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.va.oia.terminology.converters.sharedUtils;

import gov.va.oia.terminology.converters.sharedUtils.stats.ConverterUUID;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dwfa.cement.RefsetAuxiliary;
import org.dwfa.tapi.TerminologyException;
import org.ihtsdo.etypes.EConcept;
import org.ihtsdo.etypes.EConceptAttributes;
import org.ihtsdo.tk.binding.snomed.Snomed;
import org.ihtsdo.tk.binding.snomed.SnomedMetadataRf2;
import org.ihtsdo.tk.dto.concept.component.TkComponent;
import org.ihtsdo.tk.dto.concept.component.TkRevision;
import org.ihtsdo.tk.dto.concept.component.description.TkDescription;
import org.ihtsdo.tk.dto.concept.component.identifier.TkIdentifier;
import org.ihtsdo.tk.dto.concept.component.identifier.TkIdentifierUuid;
import org.ihtsdo.tk.dto.concept.component.refex.TkRefexAbstractMember;
import org.ihtsdo.tk.dto.concept.component.refex.type_uuid.TkRefexUuidMember;
import org.ihtsdo.tk.dto.concept.component.relationship.TkRelationship;

/**
 * {@link JIFHack}
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a> 
 */
public class JIFHack {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws TerminologyException 
	 */
	public static void main(String[] args) throws IOException, TerminologyException {
	
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("JIF Hack.jbin"))));
		ConverterUUID.configureNamespace(UUID.nameUUIDFromBytes("whatever".getBytes()));
		ConverterUUID.disableUUIDMap_ = true;
		
		
		//Create the Jif module concept that is not created any more.
		//Jif module
		EConcept jifModule = createConcept(UUID.fromString("2a6836d3-4782-5906-aec1-94ff862b21cb"), null, null);
		addRelationship(jifModule, UUID.fromString("40d1c869-b509-32f8-b735-836eac577a67"), jifModule.getConceptAttributes().getTime());  //module
		addDescription(jifModule, UUID.fromString("a09c49b9-b5f3-53d4-9751-825d7be39a88"), "JIF maintained module (core metadata concept)", true);
		addDescription(jifModule, UUID.fromString("e64f37e3-f6d1-5de6-9ea1-efb9c4f322d6"), "JIF maintained module", false);
		jifModule.writeExternal(dos);
		resetPaths(jifModule);
		jifModule.writeExternal(dos);
		
		//Create the VA module concept, with the JIF module as a second ID
		//va module
		//jif module
		EConcept vaModule = createConcept(UUID.fromString("fd8fe531-eda8-576f-b75b-79735529798e"), UUID.fromString("2a6836d3-4782-5906-aec1-94ff862b21cb"), null);
//		addRelationship(vaModule, UUID.fromString("40d1c869-b509-32f8-b735-836eac577a67"), vaModule.getConceptAttributes().getTime());  //module
		addDescription(vaModule, UUID.fromString("a2c0d4d6-f22d-5955-870a-baf12d7a28c7"), "VA maintained module (core metadata concept)", true);
		addDescription(vaModule, UUID.fromString("84daf425-0b6c-5bed-8996-d673fec4a2de"), "VA maintained module", false);
		vaModule.writeExternal(dos);
		resetPaths(vaModule);
		vaModule.writeExternal(dos);
		
		
//		dos.writeLong(System.currentTimeMillis());
		
		//new con
		EConcept extRefSet = createConcept(UUID.fromString("a0430463-6ddb-5e09-9e9e-e26d777972c7"), null, "VA Extension reference set (foundation metadata concept)");
		//Reference set (foundation metadata concept)
		addRelationship(extRefSet, UUID.fromString("7e38cd2d-6f1a-3a81-be0b-21e6090573c2"), extRefSet.getConceptAttributes().getTime()); 
		extRefSet.writeExternal(dos);
		resetPaths(extRefSet);
		extRefSet.writeExternal(dos);

		
//		dos.writeLong(System.currentTimeMillis());
		
		//generated UUID
		//JIF Reactants (refset)
		EConcept reactants = createConcept(UUID.fromString("931fea01-0045-5689-ae2a-fd57f5dbb900"), UUID.fromString("c439b4b1-ce66-4fa8-bad4-213d56651a81"),
				"VA Allergens (foundation metadata concept)");
		addDescription(reactants, UUID.fromString("15c17fd8-2db2-5102-b67a-5c7c7747343d"), "JIF Reactants (refset)", true);
		addDescription(reactants, null, "VA Allergens", false);
		addDescription(reactants, null, "JIF Reactants", false);
		addRelationship(reactants, extRefSet.getPrimordialUuid(), reactants.getConceptAttributes().getTime());
		reactants.setAnnotationStyleRefex(true);
		reactants.setAnnotationIndexStyleRefex(true);
		reactants.writeExternal(dos);
		resetPaths(reactants);
		reactants.writeExternal(dos);
		
//		dos.writeLong(System.currentTimeMillis());

		//make sure this concept gets created / merged
		//Food (CHDR metadata)
		//Foods (substance)
		EConcept foods2 = createConcept(UUID.fromString("06c17145-5480-4260-9696-fc4d7f259b73"), UUID.fromString("4e995b20-f506-3bfc-a831-c8c469121953"), null);
		addDescription(foods2, UUID.fromString("c3849e3e-dfce-5be2-be57-c4b955edcac1"), "Food (CHDR metadata)", true);
		addDescription(foods2, null, "Food", false);
		foods2.writeExternal(dos);
		//We shouldn't need to export this concept - so shouldn't need it on the release path, 
		//but for some reason, it doesn't merge if I don't... so, put it on both paths.
		resetPaths(foods2);  
		foods2.writeExternal(dos);
		
	
		dos.close();
	}
	
	
	private static void setRevisionAttributes(TkRevision object, Long time)
	{
		object.setAuthorUuid(UUID.fromString("f7495b58-6630-3499-a44e-2052b5fcf06c")); //user
		object.setModuleUuid(UUID.fromString("2a6836d3-4782-5906-aec1-94ff862b21cb"));  //JIF maintained module;
		object.setPathUuid(UUID.fromString("944355c4-6eee-5fef-b8cc-fb0f500878ef"));  //JIF Term Workbench origin
		object.setStatusUuid(SnomedMetadataRf2.ACTIVE_VALUE_RF2.getUuids()[0]);
		object.setTime(time.longValue());
	}
	
	private static void resetPath(TkRevision object)
	{
		object.setPathUuid(UUID.fromString("54f51020-ad78-5218-b5af-0e85a747f2a1"));  //JIF Term Workbench release candidate path
	}
	
	private static void resetPaths(EConcept concept)
	{
		resetPath(concept.getConceptAttributes());
		if (concept.getConceptAttributes().getAdditionalIdComponents() != null)
		{
			for (TkIdentifier x : concept.getConceptAttributes().getAdditionalIdComponents())
			{
				resetPath(x);
			}
		}
		if (concept.getDescriptions() != null)
		{
			for (TkDescription x : concept.getDescriptions())
			{
				resetPath(x);
				if (x.getAnnotations() != null)
				{
					for (TkRefexAbstractMember<?> y : x.getAnnotations())
					{
						resetPath(y);
					}
				}
			}
		}
		if (concept.getRelationships() != null)
		{
			for (TkRelationship x : concept.getRelationships())
			{
				resetPath(x);
			}
		}
	}
	
	public static EConcept createConcept(UUID conceptPrimordialUuid, UUID extraUUID, String fsn) throws IOException, TerminologyException
	{
		EConcept eConcept = new EConcept();
		eConcept.setPrimordialUuid(conceptPrimordialUuid);
		EConceptAttributes conceptAttributes = new EConceptAttributes();
		conceptAttributes.setDefined(false);
		conceptAttributes.setPrimordialComponentUuid(conceptPrimordialUuid);
		if (extraUUID != null)
		{
			List<TkIdentifier> extraIds = conceptAttributes.getAdditionalIdComponents();
			if (extraIds == null)
			{
				extraIds = new ArrayList<TkIdentifier>();
				conceptAttributes.setAdditionalIdComponents(extraIds);
			}
			TkIdentifierUuid id = new TkIdentifierUuid(extraUUID);
			setRevisionAttributes(id, conceptAttributes.getTime());
			extraIds.add(id);
		}
		
		setRevisionAttributes(conceptAttributes, System.currentTimeMillis());
		eConcept.setConceptAttributes(conceptAttributes);
		if (fsn != null)
		{
			addDescription(eConcept, null, fsn, true);
		}
		return eConcept;
	}
	
	public static TkDescription addDescription(EConcept eConcept, UUID valueUUID, String value, boolean isFSN) throws IOException, TerminologyException
	{
		List<TkDescription> descriptions = eConcept.getDescriptions();
		if (descriptions == null)
		{
			descriptions = new ArrayList<TkDescription>();
			eConcept.setDescriptions(descriptions);
		}
		TkDescription description = new TkDescription();
		description.setConceptUuid(eConcept.getPrimordialUuid());
		description.setLang("en");
		UUID descriptionPrimordialUUID = (valueUUID == null ? ConverterUUID.createNamespaceUUIDFromStrings(eConcept.getPrimordialUuid().toString(), value, 
					(isFSN ? "FSN" : "SYNONYM"), true + "") : valueUUID);
		description.setPrimordialComponentUuid(descriptionPrimordialUUID);
		description.setTypeUuid(isFSN ? SnomedMetadataRf2.FULLY_SPECIFIED_NAME_RF2.getUuids()[0] : SnomedMetadataRf2.SYNONYM_RF2.getUuids()[0]);
		description.setText(value);
		setRevisionAttributes(description, eConcept.getConceptAttributes().getTime());

		descriptions.add(description);
		//Add the en-us info
		addUuidAnnotation(description, SnomedMetadataRf2.PREFERRED_RF2.getUuids()[0], SnomedMetadataRf2.US_ENGLISH_REFSET_RF2.getUuids()[0], 
				eConcept.getConceptAttributes().getTime());

		return description;
	}
	
	public static TkRefexUuidMember addUuidAnnotation(TkComponent<?> component, UUID valueConcept, UUID refsetUuid, Long time) throws IOException, TerminologyException
	{
		List<TkRefexAbstractMember<?>> annotations = component.getAnnotations();

		if (annotations == null)
		{
			annotations = new ArrayList<TkRefexAbstractMember<?>>();
			component.setAnnotations(annotations);
		}

		TkRefexUuidMember conceptRefexMember = new TkRefexUuidMember();

		conceptRefexMember.setComponentUuid(component.getPrimordialComponentUuid());
		UUID annotationPrimordialUuid = ConverterUUID.createNamespaceUUIDFromStrings(component.getPrimordialComponentUuid().toString(), 
				RefsetAuxiliary.Concept.NORMAL_MEMBER.getPrimoridalUid().toString(), refsetUuid.toString());
		
		conceptRefexMember.setPrimordialComponentUuid(annotationPrimordialUuid);
		conceptRefexMember.setUuid1(valueConcept);
		conceptRefexMember.setRefsetUuid(refsetUuid);
		setRevisionAttributes(conceptRefexMember, component.getTime());

		annotations.add(conceptRefexMember);
		return conceptRefexMember;
	}
	
	public static TkRelationship addRelationship(EConcept eConcept, UUID targetUuid, Long time)
	{
		List<TkRelationship> relationships = eConcept.getRelationships();
		if (relationships == null)
		{
			relationships = new ArrayList<TkRelationship>();
			eConcept.setRelationships(relationships);
		}

		TkRelationship rel = new TkRelationship();
		rel.setPrimordialComponentUuid(ConverterUUID.createNamespaceUUIDFromStrings(eConcept.getPrimordialUuid().toString(), targetUuid.toString(), 
				Snomed.IS_A.getUuids()[0].toString()));
		rel.setC1Uuid(eConcept.getPrimordialUuid());
		rel.setTypeUuid(Snomed.IS_A.getUuids()[0]);
		rel.setC2Uuid(targetUuid);
		rel.setCharacteristicUuid(SnomedMetadataRf2.STATED_RELATIONSHIP_RF2.getUuids()[0]);
		rel.setRefinabilityUuid(SnomedMetadataRf2.NOT_REFINABLE_RF2.getUuids()[0]);
		rel.setRelGroup(0);
		setRevisionAttributes(rel, time);

		relationships.add(rel);
		return rel;
	}

}
